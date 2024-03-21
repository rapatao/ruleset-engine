# ruleset-engine

[![Maven Central](https://img.shields.io/maven-central/v/com.rapatao.ruleset/ruleset-engine.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.rapatao.ruleset%20AND%20a:ruleset-engine)
[![Sonatype OSS](https://img.shields.io/nexus/r/com.rapatao.ruleset/ruleset-engine?label=Sonatype%20OSS&server=https%3A%2F%2Foss.sonatype.org)](https://ossindex.sonatype.org/component/pkg:maven/com.rapatao.ruleset/ruleset-engine)

A simple rule engine that uses [Rhino](https://github.com/mozilla/rhino) implementation of JavaScript to evaluate
expressions.

## Get started

To get started, add the following dependency:

### Gradle

```groovy
implementation "com.rapatao.ruleset:ruleset-engine:$rulesetVersion"
```

### Maven

```xml

<dependency>
    <groupId>com.rapatao.ruleset</groupId>
    <artifactId>ruleset-engine</artifactId>
    <version>$rulesetVersion</version>
</dependency>
```

## Supported operations (expressions)

The engine only supports `boolean` evaluations, which means that all operations must results in a boolean value.

All provided operations can be created using the
builder: `com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder`

To create custom operations, just extends the interface `com.rapatao.projects.ruleset.engine.types.Expression`

| operator              | description                                             |
|-----------------------|---------------------------------------------------------|
| EQUALS                | Represents the equals operator (`==`)                   |
| NOT_EQUALS            | Represents the not equals operator (`!=`)               |
| GREATER_THAN          | Represents the greater than operator (`>`)              |
| GREATER_OR_EQUAL_THAN | Represents the greater than or equal to operator (`>=`) |
| LESS_THAN             | Represents the less than operator (`<`)                 |
| LESS_OR_EQUAL_THAN    | Represents the less than or equal to operator (`<=`)    |
| STARTS_WITH           | Represents the `startsWith` operation                   |
| ENDS_WITH             | Represents the `endsWith` operation                     |

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

"field" startsWith "\"fie\""

"field" endsWith "\"eld\""
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

## JSON Serialization

All provided operations supports serialization using [Jackson](https://github.com/FasterXML/jackson) with the definition
of a Mixin.

Mixin interface: `com.rapatao.projects.ruleset.jackson.ExpressionMixin`

Example of usage:

```kotlin
val mapper = jacksonObjectMapper()
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    .addMixIn(Expression::class.java, ExpressionMixin::class.java)

val json = "{ definition as json }"

val asMatcher: Expression = mapper.readValue(json)
```

Serialized examples can be checked [here](JSON.md)

## Usage example

```kotlin
import com.rapatao.projects.ruleset.engine.Evaluator
import com.rapatao.projects.ruleset.engine.types.builder.equalsTo

val rule = "item.price" equalsTo 0

val evaluator = Evaluator()

val result = evaluator.evaluate(rule, mapOf("item" to mapOf("price" to 0)))
println(result) // true


data class Item(val price: Double)
data class Input(val item: Item)

val result2 = evaluator.evaluate(rule, Input(item = Item(price = 0.0)))
println(result) // true
```
