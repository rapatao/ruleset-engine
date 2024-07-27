# ruleset-engine

[![Maven Central](https://img.shields.io/maven-central/v/com.rapatao.ruleset/ruleset-engine.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.rapatao.ruleset%20AND%20a:ruleset-engine)
[![Sonatype OSS](https://img.shields.io/nexus/r/com.rapatao.ruleset/ruleset-engine?label=Sonatype%20OSS&server=https%3A%2F%2Foss.sonatype.org)](https://ossindex.sonatype.org/component/pkg:maven/com.rapatao.ruleset/ruleset-engine)

Simple yet powerful rules engine that offers the flexibility of using the built-in engine and creating a custom one.

## Available Engines

Below are the available engines that can be used to evaluate expressions:

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
import com.rapatao.projects.ruleset.engine.context.EvalEngine
import com.rapatao.projects.ruleset.engine.types.builder.equalsTo

val rule = "item.price" equalsTo 0
val input = mapOf("item" to mapOf("price" to 0))

val evaluator: Evaluator = ...

val result = evaluator.evaluate(rule, input)
println(result) // true


data class Item(val price: Double)
data class Input(val item: Item)

val result2 = evaluator.evaluate(rule, Input(item = Item(price = 0.0)))
println(result) // true
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

"field" endsWith "\"value\""

"field" expContains "\"value\""

"field" expNotContains "\"value\""
````

## Supported group operations

A grouped operation is `true` when all inner operations result in:

* `anyMatch`: any operation must be evaluated as `true`
* `allMatch`: all operations must be evaluated as `true`
* `noneMatch`: all operations must be evaluated as `false`

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

## Expression serialization

### Jackson

All provided operations supports serialization using [Jackson](https://github.com/FasterXML/jackson) with the definition
of a Mixin.

Mixin interface: `com.rapatao.projects.ruleset.jackson.ExpressionMixin`

Example of usage:

```kotlin
val mapper = jacksonObjectMapper()
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    .addMixIn(Expression::class.java, ExpressionMixin::class.java)

val json = "{ serialized definition }"

val asMatcher: Expression = mapper.readValue(json)
```

Serialized examples can be checked [here](JSON.md)

> Although the example only uses `JSON` as reference, by using the given `Mix-in` class, it should support any
> serialization format provided by the Jackson library, like `YAML` and `XML`.
