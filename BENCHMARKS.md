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

Every engine replays the same rule set against the same input objects. Nothing in
`com.rapatao.projects.ruleset.engine.cases` branches on the engine, and each `bench` task reads the same three
properties in the same order, so a difference between two rows is the engine and not the workload.

Each iteration evaluates the 173 expressions from `com.rapatao.projects.ruleset.engine.cases.TestData` against the same
input object, after 100 warmup iterations. Results are printed and written to `bench_<engine>.txt`.

Each run reports throughput, the latency distribution of an iteration (avg, stddev, min, max, p50 to p99), the bytes
allocated per evaluation, and the garbage collections that ran during the measured loop. Allocation is read from the
JVM's per-thread counter on the benchmark thread, and reports `n/a` on a JVM that does not expose it.

`-PbenchWide=N` runs the same rules against the same `item`, under a root carrying `N` extra scalar fields and an `N`
element list. Nothing the rules read changes, only how much input surrounds it, which separates a per-call cost that
scales with the input from one that scales with the rule.

Both the default and the wide root are maps, and the wide one is built from the default one, so the two runs differ by
width alone and never by what the root is. That matters because the engines do not treat the two root kinds alike: a
map root is a hash lookup where a typed root is a reflective property read. Mixing the two across the columns would
price that difference as if it were width.

Two things to set up before trusting a run:

* Run at full power. On a laptop in a power saving mode the whole suite lands 25 to 30% low, uniformly across engines.
* Compare only runs from the same session. The harness is a timing loop with no confidence intervals, so a difference
  smaller than the run-to-run spread below is not a result.

## Results

2000 iterations of the 173 expression suite, 346,000 evaluations per engine, Apple M3 Pro, Amazon Corretto 21.0.11,
three runs per configuration in one session, medians below. These are relative magnitudes, not absolute figures: the
harness is a simple timing loop, not JMH, and the GraalJS run is interpreter-only because Corretto is not a GraalVM JDK.

| engine               | ops/s     | avg per iteration | stddev | p50     | p99     | relative cost |
|----------------------|-----------|-------------------|--------|---------|---------|---------------|
| Kotlin               | 2,598,127 | 67us              | 58us   | 49us    | 234us   | 1x            |
| Rhino                | 325,099   | 522us             | 185us  | 446us   | 1.36ms  | ~8.0x         |
| GraalJS (reused ctx) | 250,370   | 678us             | 478us  | 550us   | 2.43ms  | ~10.4x        |
| GraalJS              | 9,026     | 18.90ms           | 1.30ms | 18.55ms | 22.89ms | ~288x         |

Run-to-run spread differs by engine, and sets how large a difference has to be before it means anything:

| engine               | observed across runs   | p99 vs p50 |
|----------------------|------------------------|------------|
| Kotlin               | 2,384,000 to 2,598,000 | ~4.8x      |
| Rhino                | 318,000 to 332,000     | ~3.1x      |
| GraalJS (reused ctx) | 243,000 to 255,000     | ~4.4x      |
| GraalJS              | 8,863 to 9,156         | ~1.2x      |

The three fast configurations move by about 10% across runs and their p99 is 3 to 5 times their p50. The tail is GC
and JIT, not the engine, so read a difference smaller than that as noise. Default GraalJS is the exception on both
counts: an iteration is so dominated by context creation that nothing else is visible in it.

The Kotlin engine is the least stable measurement here across *sessions*, even though its three runs above sit within
9% of each other. Its iteration is about 67us, short enough that the loop measures the JVM as much as the engine, so
compare its number only against another run from the same session.

### Allocation

Bytes allocated per `evaluate`, counted on the benchmark thread by the JVM's own allocation counter, and the garbage
collections that ran during the measured loop:

| engine               | alloc per evaluation | vs Kotlin | gc during the run |
|----------------------|----------------------|-----------|-------------------|
| Kotlin               | 375 B                | 1x        | 1, 2ms            |
| GraalJS (reused ctx) | 5,744 B              | ~15.3x    | 16, 20ms          |
| Rhino                | 11,462 B             | ~30.6x    | 18, 14ms          |
| GraalJS              | 128,094 B            | ~342x     | 282, 139ms        |

This is the steadiest number the harness produces: it varies by under 2% across runs, where throughput varies by 10%.

The order is not the throughput order. Reused-context GraalJS allocates half of what Rhino does per evaluation and is
still slower, so Rhino's cost is not allocation-bound: it compiles a fresh script per operator invocation, and
compilation is work rather than garbage. Default GraalJS allocates a whole polyglot `Context` per call, which is
the 342x.

### Input width

The same run with `-PbenchWide=200`: identical rules reading identical fields, under a root carrying 200 extra scalar
fields and a 200 element list. Both columns root at a map, so the width is the only difference between them.

| engine               | ops/s default | ops/s wide(200) | wide is       | alloc default | alloc wide(200) | wide allocates |
|----------------------|---------------|-----------------|---------------|---------------|-----------------|----------------|
| Kotlin               | 2,598,127     | 2,489,220       | unchanged     | 375 B         | 384 B           | unchanged      |
| Rhino                | 325,099       | 135,358         | ~2.4x slower  | 11,462 B      | 23,729 B        | ~2.1x more     |
| GraalJS (reused ctx) | 250,370       | 16,471          | ~15.2x slower | 5,744 B       | 85,539 B        | ~14.9x more    |
| GraalJS              | 9,026         | 5,924           | ~1.5x slower  | 128,094 B     | 208,110 B       | ~1.6x more     |

Each factor compares the two columns to its left, within the same row. A row varies the input only: the engine and its
configuration are held constant across it, so `reuseContextPerThread` is on in both columns of the reused row and off
in both columns of the row below it. The Results table above prices the reuse setting.

For both JS engines the allocation factor tracks the throughput factor, which identifies the cost: they inject every
top-level entry of the input into the scope on every `evaluate`, and pay for it whether a rule reads it or not. Neither
pays for *depth*, since a nested object is handed over whole and JS walks into it lazily. Reused-context GraalJS runs
157 collections over the wide input against 16 over the default one, and Rhino 34 against 18.

Default GraalJS shows the smallest factor because context creation, at ~18.9ms per iteration, dominates the injection.
In the reused-context mode the injection is the dominant remaining cost.

The Kotlin engine resolves the paths a rule names and never visits the rest, so its cost tracks the rule and not the
input: its two columns are flat in both throughput and allocation, across a root that carries 202 entries instead
of 1.

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
parsing and the removal of input flattening, so read it as history rather than as the engine's current cost: the
suite tables above are the current measurement.

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
  call, worth 2.4x on Rhino and 15.2x on reused-context GraalJS for 200 extra fields. Nesting the parts a rule does not
  read one level deeper avoids it. The Kotlin engine reads only the paths a rule names and is flat here
* Watch allocation, not just throughput, if the service is latency-sensitive: an evaluation costs 375 B on the Kotlin
  engine and 128 KB on default GraalJS, and that is what fills the nursery and sets the GC rate under load
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
thread, worth roughly 28x (18.9ms to 678us per iteration), at the cost of rules on one thread sharing a context. Keep
it off for untrusted rules or rules that write globals.
