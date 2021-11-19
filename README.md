# ruleset-engine

A simple rule engine that uses [Rhino](https://github.com/mozilla/rhino) implementation of JavaScript to evaluate expressions.

## Supported operations (expressions)

The engine only supports `boolean` evaluations, which means that all operations must results in a boolean value.

All provided operations can be created using the
builder: `com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder`

To create custom operations, just extends the interface `com.rapatao.projects.ruleset.engine.types.Expression`

## Supported group operations

A grouped operation is `true` when all inner operations result in:

* `anyMatch`: any operation must be evaluated as `true`
* `allMatch`: all operations must be evaluated as `true`
* `noneMatch`: all operations must be evaluated as `false`

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

val asMatcher: Matcher = mapper.readValue(json)
```

### JSON examples

```json
{"expression":{"left":"field","operator":"EQUALS","right":true}}
```
```json
{"expression":{"left":"field","operator":"EQUALS","right":10}}
```
```json
{"expression":{"left":"field","operator":"EQUALS","right":"\"value\""}}
```
```json
{"expression":{"left":"field","operator":"EQUALS","right":"value"}}
```
```json
{"expression":{"value":"field","from":10,"to":20}}
```
```json
{"expression":{"left":"field","operator":"GREATER_THAN","right":10}}
```
```json
{"expression":{"left":"field","operator":"GREATER_OR_EQUAL_THAN","right":10}}
```
```json
{"expression":{"left":"field","operator":"LESS_THAN","right":10}}
```
```json
{"expression":{"left":"field","operator":"LESS_OR_EQUAL_THAN","right":10}}
```
```json
{"allMatch":[{"expression":{"left":"field","operator":"EQUALS","right":true}},{"expression":{"left":"price","operator":"LESS_THAN","right":10.0}}]}
```
```json
{"anyMatch":[{"expression":{"left":"field","operator":"EQUALS","right":true}},{"expression":{"left":"price","operator":"LESS_THAN","right":10.0}}]}
```
```json
{"noneMatch":[{"expression":{"left":"field","operator":"EQUALS","right":true}},{"expression":{"left":"price","operator":"LESS_THAN","right":10.0}}]}
```
```json
{"allMatch":[{"expression":{"left":"field","operator":"EQUALS","right":true}},{"expression":{"left":"price","operator":"LESS_THAN","right":10.0}}],"anyMatch":[{"expression":{"left":"field","operator":"EQUALS","right":true}},{"expression":{"left":"price","operator":"LESS_THAN","right":10.0}}],"noneMatch":[{"expression":{"left":"field","operator":"EQUALS","right":true}},{"expression":{"left":"price","operator":"LESS_THAN","right":10.0}}]}
```
