# ruleset-engine

[![Maven Central](https://img.shields.io/maven-central/v/com.rapatao.ruleset/ruleset-engine.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.rapatao.ruleset%20AND%20a:ruleset-engine)

Simple yet powerful rules engine that offers the flexibility of using the built-in engine and creating a custom one.

## Available Engines

Below are the available engines that can be used to evaluate expressions. All of them implement the same
`com.rapatao.projects.ruleset.engine.Evaluator` contract and accept the same `Expression` tree, so switching engine is a
matter of changing the dependency and the instantiation line.

| engine  | operands         | best fit                                           |
|---------|------------------|----------------------------------------------------|
| Kotlin  | field paths only | high volume, plain comparison rules                |
| Rhino   | JavaScript       | rules that need scripting, high volume             |
| GraalJS | JavaScript       | modern ECMAScript, GraalVM deployments, low volume |

Throughput, run-to-run spread, where the time goes inside each engine and tuning guidance are in
[BENCHMARKS.md](BENCHMARKS.md).

### Kotlin engine implementation

This engine uses only Kotlin code to support all Operator functions, offering expressive performance. Although it
doesn't support Kotlin expressions inside the expression operands, it can be a suitable choice for simpler rule sets or
projects where you prefer using a statically-typed language like Kotlin.

Supported types:

1. primitive Java types, boolean, string, number (extends)
2. custom objects (reflection)
3. maps
4. lists
5. arrays

```kotlin
val evaluator = com.rapatao.projects.ruleset.engine.evaluator.kotlin.KotlinEvaluator()
```

#### How it works

Operands that are field paths (`item.price`, `item.tags[0]`, ...) are resolved against the input on demand, one path
at a time: maps are read by key, collections and arrays by index, and arbitrary objects by Kotlin reflection
(`memberProperties`, reflected once per class and cached). Only the nodes a path names are visited, so the cost tracks
the rule rather than the input. Operators are plain Kotlin functions (`==`, `>`, `String.contains`, ...).

Numbers are normalised to `BigDecimal`, elements of a list included, so an `Int` operand and a `BigDecimal` field
compare as expected and `listOf(1, 2) expContains 1` matches. They compare by value rather than by representation, so
`10` and `10.00` are the same number and a fraction is never truncated.

A path that does not exist throws, which `onFailure` turns into a rule result. A path exists when every step of it
does: a map holds the key, an object has the property, an index is in range. Nothing exists below a `null`, a string
or a number, so `item.name.length` throws rather than resolving through reflection.

Operands are literals or field paths only. A quoted operand (`"\"value\""`) is a string literal, an unquoted one is
first tried as a number or boolean literal and then as a field path. There is no expression language, so
`item.price * 2` is not supported: model it as a field on the input, or as a custom operator.

A list written in an expression holds operands, and each element is resolved by those same rules:

```kotlin
// the literal "test", and whatever item.name holds
listOf("\"test\"", "item.name") expContains "\"product name\""

// looks for fields named test and brand-new, and throws when the input has neither
listOf("test", "brand-new") expContains "\"test\""
```

An unquoted string element is a field path, exactly as an unquoted scalar operand is. Quote it to compare against the
text itself.

#### Best for

* Rule sets made of comparisons over data you already have in memory
* Hot paths where evaluation cost matters more than rule expressiveness
* Environments where shipping a script interpreter is unwanted (smaller dependency surface, no scripting sandbox to
  reason about)

#### Trade-offs

* No expressions in operands
* Reflection is used for non-map inputs; passing a `Map` avoids it. The properties of each class are reflected once
  and cached, so the cost falls on the first evaluation against a given input type
* The cache is keyed by `Class` and never evicted, which pins classloaders in a container that redeploys

#### Gradle

```groovy
implementation "com.rapatao.ruleset:kotlin-evaluator:$rulesetVersion"
```

#### Maven

```xml

<dependency>
    <groupId>com.rapatao.ruleset</groupId>
    <artifactId>kotlin-evaluator</artifactId>
    <version>$rulesetVersion</version>
</dependency>
```

### Mozilla Rhino (JavaScript) engine implementation

[Mozilla Rhino](https://github.com/mozilla/rhino) is an open-source, embeddable JavaScript interpreter from Mozilla.
This engine implementation supports using JavaScript expressions inside the rule operands and is particularly useful
when rules contain complex logic or when you want to leverage JavaScript's extensive library of functions.

```kotlin
val evaluator = com.rapatao.projects.ruleset.engine.evaluator.rhino.RhinoEvaluator()
```

#### How it works

The JavaScript standard objects (`Object`, `Array`, `String`, `Math`, ...) are built once per evaluator instance and
sealed. Each `evaluate` call then obtains a Rhino `Context` and creates a cheap child scope that has those sealed
standard objects as its prototype, and injects the input data into that child (maps by key, other objects by Kotlin
reflection). Every operator then builds a small JavaScript snippet (`true == ((left) == (right))`) and compiles and
executes it in that scope, which means both operands are arbitrary JavaScript.

Because the input lands on the per-evaluation child and never on the shared parent, one evaluation cannot see another
one's bindings, a global defined by a rule dies with the evaluation that defined it, and `evaluate` stays safe to call
concurrently from any number of threads.

The context is created through `RhinoContextFactory`, which is where the engine is tuned:

```kotlin
val evaluator = RhinoEvaluator(
    contextFactory = RhinoContextFactory(
        interpretedMode = false, // compile to bytecode instead of interpreting
        languageVersion = Context.VERSION_ES6,
    )
)
```

`interpretedMode` defaults to `true`, and that default is the fast one for this engine. Because a fresh snippet is
compiled per operator invocation and never cached, bytecode generation cost is paid on every evaluation and never
amortised, which makes `interpretedMode = false` far slower on the benchmark rule set
(see [BENCHMARKS.md](BENCHMARKS.md)). Leave it as is unless you have measured your own workload.

#### Best for

* Rules that need real expressions in the operands (`item.price * quantity`, `item.name.toLowerCase()`, ternaries,
  inline functions)
* Rules authored or edited outside the codebase, for example loaded from JSON at runtime
* Plain JVM deployments: Rhino is a small pure Java dependency with no native image or JDK requirements

#### Trade-offs

* Slower than the Kotlin engine, faster than GraalJS (see [BENCHMARKS.md](BENCHMARKS.md))
* JavaScript language support is behind GraalJS; set `languageVersion` explicitly if you need ES6 syntax
* The standard objects are sealed, so a rule cannot monkey-patch a builtin (`Array.prototype.foo = ...` throws)
* The whole input is injected per `evaluate` call, even when the rule reads a single field
* Each operator still compiles its snippet on every invocation; compiled scripts are not cached

#### Gradle

```groovy
implementation "com.rapatao.ruleset:rhino-evaluator:$rulesetVersion"
```

#### Maven

```xml

<dependency>
    <groupId>com.rapatao.ruleset</groupId>
    <artifactId>rhino-evaluator</artifactId>
    <version>$rulesetVersion</version>
</dependency>
```

### GraalVM (JavaScript) engine implementation

[GraalJS](https://www.graalvm.org/latest/reference-manual/js/) is a high-performance JavaScript engine.
This engine implementation supports using JavaScript expressions inside the rule operands and is particularly useful
when rules contain complex logic or when you want to leverage JavaScript's extensive library of functions.

```kotlin
val evaluator = com.rapatao.projects.ruleset.engine.evaluator.graaljs.GraalJSEvaluator()
```

#### How it works

The evaluator holds a shared polyglot `Engine` and, by default, builds a new `Context` per `evaluate` call and closes it
afterwards. Input data is injected into a fresh JavaScript object created for that evaluation (maps by key, other
objects by Kotlin reflection with `HostAccess.ALL`), and each operator evaluates a JavaScript `Source` resolved against
that object, so both operands are arbitrary JavaScript.

#### Reusing the context

Building a `Context` is what the engine spends almost all of its time on. `reuseContextPerThread` keeps one context per
thread instead, which is the single largest change available on this engine
(see [BENCHMARKS.md](BENCHMARKS.md)):

```kotlin
val evaluator = GraalJSEvaluator(reuseContextPerThread = true)
```

| | default (`false`) | `reuseContextPerThread = true` |
|---------------------------------|--------------------------------|-------------------------------------------------|
| context lifetime | built and closed per `evaluate` | one per thread, alive as long as the thread |
| concurrent `evaluate` | safe | safe, each thread has its own context |
| input bindings between calls | isolated | isolated, the input object is replaced per call |
| globals a rule writes | discarded with the context | visible to later calls on the same thread |

So the reused mode is safe to call concurrently and never leaks input data between evaluations, but a rule that writes
to `globalThis` or redefines a builtin affects later evaluations on the same thread. Use it with a bounded thread pool:
per-thread contexts are not closed, so unbounded thread creation retains them.

Both the `Engine` and the `Context.Builder` are constructor parameters, which is where the engine is tuned:

```kotlin
val evaluator = GraalJSEvaluator(
    contextBuilder = Context.newBuilder()
        .engine(engine)
        .option("js.ecmascript-version", "2023")
        .allowHostAccess(HostAccess.EXPLICIT) // narrower than the default HostAccess.ALL
)
```

The default builder enables `HostAccess.ALL`, `allowHostClassLookup { true }` and `js.nashorn-compat`. That is
convenient, but it lets rule authors reach arbitrary JVM classes from a rule. If rules come from an untrusted source,
pass a restricted `Context.Builder`.

#### Best for

* Modern ECMAScript in the operands (the default is `js.ecmascript-version` 2023)
* Applications already running on GraalVM, where the Graal JIT compiles the rule scripts instead of interpreting them
* Rule sets where evaluation is not on a hot path, for example batch or request-scoped decisions with a low call rate

#### Trade-offs

* The slowest of the three engines when left on the default context handling, and most of that cost is context creation
  rather than the rule itself. `reuseContextPerThread = true` removes it, at the cost of the global-state isolation
  described above
* On a stock (non-GraalVM) JDK, Truffle runs in interpreter-only mode. The evaluator sets
  `engine.WarnInterpreterOnly=false`, so the usual warning is not printed. Running on a GraalVM JDK, or adding the Graal
  compiler to the runtime classpath, is what unlocks its performance
* The polyglot dependencies are considerably heavier than Rhino's single jar

#### Gradle

```groovy
implementation "com.rapatao.ruleset:graaljs-evaluator:$rulesetVersion"
```

#### Maven

```xml

<dependency>
    <groupId>com.rapatao.ruleset</groupId>
    <artifactId>graaljs-evaluator</artifactId>
    <version>$rulesetVersion</version>
</dependency>
```

## Get started

After adding the desired engine as the application dependency, copy and past the following code, replacing
the `val evaluator: Evaluator = ...` by the desired engine initialization instruction.

The following example initializes an `Evaluator`, and check if the given `rule` is valid to the given `input` data,
printing the `result` in the default output.

### Code example

```kotlin
import com.rapatao.projects.ruleset.engine.Evaluator
import com.rapatao.projects.ruleset.engine.types.builder.extensions.equalsTo

val rule = "item.price" equalsTo 0
val input = mapOf("item" to mapOf("price" to 0))

val evaluator: Evaluator = ...

val result = evaluator.evaluate(rule, input)
println(result) // true


data class Item(val price: Double)
data class Input(val item: Item)

val result2 = evaluator.evaluate(rule, Input(item = Item(price = 0.0)))
println(result2) // true
```

## Expressions (Rule)

In the context of the engine, an expression is a decision table, where many statements can be executed using defined
operators, resulting in a `boolean`, where `true` means that the given input data matches, and `false` when it doesn't
match.

All provided operations can be created using the
builder: `com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder`

### Operators

The engine provides many built-in operators, but it also allows adding new ones or event overwriting the existing one.

#### Built-in operators

| operator              | description                                                                                                                                          |
|-----------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| equals                | Represents the equality operator (==), used to check if two values are equal.                                                                        |
| not_equals            | Represents the inequality operator (!=), used to check if two values are not equal.                                                                  |
| greater_than          | Represents the greater than operator (>), used to compare if one value is greater than another.                                                      |
| greater_or_equal_than | Represents the greater than or equal to operator (>=), used to compare if one value is greater than or equal to another.                             |
| less_than             | Represents the less than operator (<), used to compare if one value is less than another.                                                            |
| less_or_equal_than    | Represents the less than or equal to operator (<=), used to compare if one value is less than or equal to another.                                   |
| starts_with           | Represents the operation to check if a string starts with a specified sequence of characters.                                                        |
| not_starts_with       | Represents the operation to check if a string not starts with a specified sequence of characters.                                                    |
| ends_with             | Represents the operation to check if a string ends with a specified sequence of characters.                                                          |
| not_ends_with         | Represents the operation to check if a string not ends with a specified sequence of characters.                                                      |
| contains              | Represents the operation to check if a string contains a specified sequence of characters or if an array/list contains a particular element.         |
| not_contains          | Represents the operation to check if a string not contains a specified sequence of characters or if an array/list not contains a particular element. |

#### Customizing the operators

It is possible to create custom operators by creating an implementation of the
interface `com.rapatao.projects.ruleset.engine.types.operators.Operators`.

The function `name()` identifies the operator, which is used when evaluating the expressions. The engine supports a
single Operator per name, which means that it is not possible to have more than one using the same name.

> Each built-in operator has its own class and all of them are located at the
> package `com.rapatao.projects.ruleset.engine.types.operators`. To override then it is not mandatory to use these base
> classes, it only needs to have the same name as the built-in operator.

There is no validation related to duplicated operator names, since it is required to allow overriding the built-in
operator by one implemented by the user of this library.

### Examples

````kotlin
"field".isTrue()

"field".isFalse()

"field" equalsTo 10

"field" equalsTo "\"value\""

"field" equalsTo "value"

"field" notEqualsTo 10

"field" notEqualsTo "\"value\""

"field" notEqualsTo "value"

"field" greaterThan 10

"field" greaterOrEqualThan 10

"field" lessThan 10

"field" lessOrEqualThan 10

"field" startsWith "\"value\""

"field" notStartsWith "\"value\""

"field" endsWith "\"value\""

"field" notEndsWith "\"value\""

"field" expContains "\"value\""

"field" expNotContains "\"value\""
````

## Supported group operations

A grouped operation is evaluated as follows:

* `anyMatch`: at least one inner expression must evaluate to `true`
* `allMatch`: all inner expressions must evaluate to `true`
* `noneMatch`: all inner expressions must evaluate to `false`

### Examples

````kotlin
allMatch(
    "field".isTrue(),
    "price" lessThan 10.0,
),

anyMatch(
    "field".isTrue(),
    "price" lessThan 10.0,
),

noneMatch(
    "field".isTrue(),
    "price" lessThan 10.0,
),

Expression(
    allMatch = listOf(
        "field".isTrue(),
        "price" lessThan 10.0,
    ),
    anyMatch = listOf(
        "field".isTrue(),
        "price" lessThan 10.0,
    ),
    noneMatch = listOf(
        "field".isTrue(),
        "price" lessThan 10.0,
    )
)
````

## Range (between) expressions

Range expressions can be composed using the `from`/`fromInclusive` extensions combined with `to`/`toInclusive`.

```kotlin
import com.rapatao.projects.ruleset.engine.types.builder.extensions.from
import com.rapatao.projects.ruleset.engine.types.builder.extensions.fromInclusive

// price > 10 AND price < 20
"price" from 10 to 20

// price >= 10 AND price <= 20
"price" fromInclusive 10 toInclusive 20
```

## Failure handling

Each `Expression` accepts an `onFailure` strategy that controls what happens when its evaluation throws (for example,
when a referenced field is missing from the input data).

| value   | behavior                                                                    |
|---------|-----------------------------------------------------------------------------|
| `THROW` | (default) re-throws the underlying exception                                |
| `TRUE`  | swallows the exception and treats the expression as `true`                  |
| `FALSE` | swallows the exception and treats the expression as `false`                 |

The strategy can be set directly on the `Expression` constructor or applied to an existing expression via the
`ifFail` extension:

```kotlin
import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.builder.extensions.equalsTo
import com.rapatao.projects.ruleset.engine.types.builder.extensions.ifFail

"item.optional.field" equalsTo 10 ifFail OnFailure.FALSE
```

## Expression serialization

### Jackson

All provided operations support serialization using [Jackson](https://github.com/FasterXML/jackson) with the definition
of a Mixin. The project currently targets Jackson 3.x (`tools.jackson` namespace).

Mixin interface: `com.rapatao.projects.ruleset.jackson.ExpressionMixin`

Example of usage:

```kotlin
import com.fasterxml.jackson.annotation.JsonInclude
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.jackson.ExpressionMixin
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.jacksonMapperBuilder
import tools.jackson.module.kotlin.readValue

val mapper: JsonMapper = jacksonMapperBuilder()
    .changeDefaultPropertyInclusion { inclusion ->
        inclusion.withValueInclusion(JsonInclude.Include.NON_NULL)
    }
    .addMixIn(Expression::class.java, ExpressionMixin::class.java)
    .build()

val json = "{ serialized definition }"

val asMatcher: Expression = mapper.readValue(json)
```

Serialized examples can be checked [here](JSON.md)

> Although the example only uses `JSON` as reference, by using the given `Mix-in` class, it should support any
> serialization format provided by the Jackson library, like `YAML` and `XML`.
