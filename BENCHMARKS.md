# Benchmarks

Measured performance of the engines shipped in this repository, how to reproduce it, and what the numbers mean when
choosing and tuning an engine. See the [README](README.md) for what each engine is and how to use it.

## Running the benchmark

Every evaluator module ships a `bench` task that replays the full test rule set against its engine:

```shell
./gradlew :kotlin-evaluator:bench
./gradlew :rhino-evaluator:bench
./gradlew :graaljs-evaluator:bench -PbenchIterations=5000
./gradlew :graaljs-evaluator:bench -PbenchIterations=5000 -PbenchReuse=true
./gradlew :kotlin-evaluator:bench -PbenchWide=200
```

Each iteration evaluates the 147 expressions from `com.rapatao.projects.ruleset.engine.cases.TestData` against the same
input object, after 100 warmup iterations. Results are printed and written to `bench_<engine>.txt`.

`-PbenchWide=N` runs the same rules against the same `item`, under a root carrying `N` extra scalar fields and an `N`
element list. Nothing the rules read changes, only how much input surrounds it, which separates a per-call cost that
scales with the input from one that scales with the rule.

Two things to set up before trusting a run:

* Run at full power. On a laptop in a power saving mode the whole suite lands 25 to 30% low, uniformly across engines.
* Compare only runs from the same session. The harness is a timing loop with no confidence intervals, so a difference
  smaller than the run-to-run spread below is not a result.

## Results

2000 iterations (294,000 evaluations per engine), Apple M3 Pro, Amazon Corretto 21.0.11, three runs per configuration
in one session at full power, medians below. These are relative magnitudes, not absolute figures: the harness is a
simple timing loop, not JMH, and the GraalJS run is interpreter-only because Corretto is not a GraalVM JDK.

| engine               | ops/s     | avg per iteration | p50     | p99     | relative cost |
|----------------------|-----------|-------------------|---------|---------|---------------|
| Kotlin               | 2,128,272 | 69us              | 60us    | 164us   | 1x            |
| Rhino                | 385,369   | 381us             | 314us   | 1.09ms  | ~5.5x         |
| GraalJS (reused ctx) | 241,141   | 610us             | 506us   | 2.11ms  | ~8.8x         |
| GraalJS              | 8,997     | 16.34ms           | 16.14ms | 18.55ms | ~236x         |

Run-to-run spread differs sharply by engine, and sets how large a difference has to be before it means anything:

| engine               | observed across runs   | p99 vs p50 |
|----------------------|------------------------|------------|
| Kotlin               | 2,119,000 to 2,427,000 | ~2.7x      |
| Rhino                | 353,000 to 388,000     | ~3.5x      |
| GraalJS (reused ctx) | 235,000 to 250,000     | ~4.2x      |
| GraalJS              | 8,900 to 9,600         | ~1.1x      |

Kotlin is the least stable. It builds neither a context nor a flattened input per call, so an iteration is short
enough that the loop measures JIT and GC noise as much as the engine. Default GraalJS is the opposite: an iteration is
so dominated by context creation that nothing else is visible.

### Input width

The same run with `-PbenchWide=200`: identical rules reading identical fields, under a root carrying 200 extra scalar
fields and a 200 element list.

| engine               | default   | wide(200) | cost of the width |
|----------------------|-----------|-----------|-------------------|
| Kotlin               | 2,128,272 | 2,047,817 | ~1.0x             |
| Rhino                | 385,369   | 149,007   | ~2.6x             |
| GraalJS (reused ctx) | 241,141   | 16,137    | ~14.9x            |
| GraalJS              | 8,997     | 5,965     | ~1.5x             |

The Kotlin engine resolves the paths a rule names and never visits the rest, so its cost tracks the rule.

Both JS engines inject every top-level entry of the input into the scope on every `evaluate`, so they pay for width
whether a rule reads it or not. Neither pays for *depth*: nested objects are handed over whole and JS walks into them
lazily. Default GraalJS shows the smallest factor because context creation, at ~16ms per iteration, dominates the
injection; in reused-context mode the injection is the dominant remaining cost.

`GraalJS (reused ctx)` is the same engine with `reuseContextPerThread = true`. Closing the per-call context and
injecting the input into a per-evaluation object costs the default mode about 4% (9,750 to 9,391 ops/s), and buys
deterministic context release plus binding isolation that holds under reuse.

## Where the time goes

The `Evaluator` contract sets up a fresh evaluation context on every `evaluate` call. Measuring a single rule
(`item.price equalsTo 10`) separates that fixed cost from the actual rule evaluation:

| engine               | one `evaluate` call | context setup | setup share |
|----------------------|---------------------|---------------|-------------|
| Kotlin               | 1.08us              | 0.88us        | ~82%        |
| Rhino                | 1.24us              | 0.17us        | ~14%        |
| GraalJS (reused ctx) | 2.19us              | 1.40us        | ~64%        |
| GraalJS              | 133.7us             | 113.5us       | ~85%        |

These rows come from one tight loop over a single rule, after 50,000 warmup calls, so they isolate the steady-state
cost. They are not comparable to the suite numbers above, which include cold and JIT-transient iterations. That loop is
not part of this repository and the `bench` tasks do not reproduce it. The Kotlin row predates both the current operand
parsing and the removal of input flattening.

Reading of the table:

* **Kotlin**: the 0.88us of setup was flattening the whole input graph into a map of every path, which scaled with the
  size of the input rather than the rule. That step is gone. Setup is now a constructor call, operand paths are
  resolved on demand, and the cost tracks the rule: widening the input to 200 extra fields and a 200 element list cost
  11.7x under flattening and costs nothing measurable now
* **Rhino**: setup is entering a `Context`, creating a child scope and injecting the input, because the standard
  objects are shared. Before that change the same two columns read 20.5us and 13.0us, a ~63% share. What is left is
  compiling and running one small script per operator, which is why deep rule trees cost more than the numbers for a
  single rule suggest. The injection half of that setup is what the input width table above prices
* **GraalJS**: context creation dominates almost entirely. On this setup the rule itself is nearly free compared to the
  polyglot context it runs in, which is what `reuseContextPerThread = true` removes. What remains once it is removed is
  injecting the input, the cost that grows with the input

## Practical guidance

* Reuse the evaluator instance. Operators are resolved once in the constructor, and for GraalJS the shared `Engine`
  caches parsed sources across contexts, so a new evaluator per request throws that away
* On GraalJS, set `reuseContextPerThread = true` unless rules are untrusted or deliberately write globals. It is the
  single largest win available on that engine
* On the JS engines, pass the narrowest input object that satisfies the rule: both inject every top-level entry per
  call, worth 2.6x on Rhino and 14.9x on reused-context GraalJS for 200 extra fields. Nesting the parts a rule does not
  read one level deeper avoids it. The Kotlin engine reads only the paths a rule names and is flat here
* Prefer `Map` inputs over arbitrary objects when the data is already in that shape: the object path goes through
  Kotlin reflection. On the Kotlin engine this is now a small difference, since the properties of each class are
  reflected once and cached
* Order `anyMatch` cheaply-first and `allMatch` most-selective-first. Evaluation short-circuits, and with the JS engines
  every skipped expression is a script that is never compiled
* On GraalJS, run on a GraalVM JDK (or put the Graal compiler on the runtime classpath) before drawing conclusions from
  its numbers. Interpreter-only mode is the default penalty on a stock JDK
* On Rhino, keep the default `interpretedMode = true`. Compiled mode measures about 100x slower, because each operator
  compiles a new script that is thrown away

Both JS engines used to rebuild their whole evaluation environment per `evaluate` call, and that dominated their cost.
Rhino no longer does: it shares one sealed set of standard objects and gives each evaluation a child scope, worth about
7.4x on this suite (3.07ms to 417us per iteration) with no loss of isolation, so there is nothing to opt into. On
GraalJS the equivalent is opt-in because it does trade isolation: `reuseContextPerThread = true` keeps one context per
thread, worth roughly 25x (15.6ms to about 0.6ms), at the cost of rules on one thread sharing a context. Keep it off
for untrusted rules or rules that write globals.
