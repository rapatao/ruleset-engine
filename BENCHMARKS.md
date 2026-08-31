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
```

Each iteration evaluates the 147 expressions from `com.rapatao.projects.ruleset.engine.cases.TestData` against the same
input object, after 100 warmup iterations. Results are printed and written to `bench_<engine>.txt`.

Two things to set up before trusting a run:

* Run at full power. On a laptop in a power saving mode the whole suite lands 25 to 30% low, uniformly across engines.
* Compare only runs from the same session. The harness is a timing loop with no confidence intervals, so a difference
  smaller than the run-to-run spread below is not a result.

## Results

2000 iterations (294,000 evaluations per engine), Apple M3 Pro, Amazon Corretto 21.0.11. These are relative
magnitudes, not absolute figures: the harness is a simple timing loop, not JMH, and the GraalJS run is interpreter-only
because Corretto is not a GraalVM JDK.

| engine               | ops/s    | avg per iteration | p50       | p99       | relative cost |
|----------------------|----------|-------------------|-----------|-----------|---------------|
| Kotlin               | 782,637  | 188us             | 166us     | 331us     | 1x            |
| Rhino                | 352,564  | 417us             | 340us     | 1.18ms    | ~2.2x         |
| GraalJS (reused ctx) | ~240,000 | ~590us            | ~500us    | ~2.0ms    | ~3.3x         |
| GraalJS              | 9,391    | 15.65ms           | 15.57ms   | 17.36ms   | ~83x          |

Each row is one representative run. Run-to-run spread differs sharply by engine, and sets how large a difference has to
be before it means anything:

| engine               | observed across runs | p99 vs p50 |
|----------------------|----------------------|------------|
| Kotlin               | 778,000 to 791,000   | ~2x        |
| Rhino                | 286,000 to 394,000   | ~3.5x      |
| GraalJS (reused ctx) | 185,000 to 294,000   | ~4x        |
| GraalJS              | stable within a few % | ~1.1x     |

The two fastest configurations are the least stable. Once the per-evaluation context cost is gone, an iteration is
short enough that the loop measures JIT and GC noise as much as the engine. Default GraalJS is the opposite: an
iteration is so dominated by context creation that nothing else is visible.

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
not part of this repository and the `bench` tasks do not reproduce it. The Kotlin row predates the current operand
parsing, which cut per-operand work and not context setup, so its real setup share is above the ~82% shown.

Reading of the table:

* **Kotlin**: the fixed cost is flattening the input graph, and it is nearly the whole cost. It scales with the size of
  the input object, not with the rule, so a wide input evaluated against a two-field rule pays for every other field
* **Rhino**: setup is entering a `Context`, creating a child scope and injecting the input, because the standard
  objects are shared. Before that change the same two columns read 20.5us and 13.0us, a ~63% share. What is left is
  compiling and running one small script per operator, which is why deep rule trees cost more than the numbers for a
  single rule suggest
* **GraalJS**: context creation dominates almost entirely. On this setup the rule itself is nearly free compared to the
  polyglot context it runs in, which is what `reuseContextPerThread = true` removes

## Practical guidance

* Reuse the evaluator instance. Operators are resolved once in the constructor, and for GraalJS the shared `Engine`
  caches parsed sources across contexts, so a new evaluator per request throws that away
* On GraalJS, set `reuseContextPerThread = true` unless rules are untrusted or deliberately write globals. It is the
  single largest win available on that engine
* Pass the narrowest input object that satisfies the rule. All three engines materialise the whole input per call
* Prefer `Map` inputs over arbitrary objects when the data is already in that shape: the object path goes through
  Kotlin reflection
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
