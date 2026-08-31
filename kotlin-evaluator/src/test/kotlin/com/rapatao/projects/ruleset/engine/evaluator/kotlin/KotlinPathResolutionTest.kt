package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import com.rapatao.projects.ruleset.engine.types.builder.extensions.equalsTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Locks the path resolution semantics the flat-map implementation had: which paths resolve, which resolve to null,
 * and which throw. `OnFailure` turns the throw into a rule result, so the difference is observable.
 */
class KotlinPathResolutionTest {

    data class Holder(val value: String?, val nested: Holder? = null)

    private val evaluator = KotlinEvaluator()

    private fun resolves(path: String, expected: Any?, input: Any) =
        assertThat(path, evaluator.evaluate(path equalsTo expected, input), equalTo(true))

    private fun absent(path: String, input: Any) =
        assertThrows<NoSuchElementException>(path) { evaluator.evaluate(path equalsTo 1, input) }

    @Test
    @DisplayName("resolves a map value, a nested map value and the map itself")
    fun assertMapPaths() {
        val input = mapOf("a" to mapOf("b" to "c"))

        resolves("a.b", "\"c\"", input)
        resolves("a", mapOf("b" to "c"), input)
    }

    @Test
    @DisplayName("a map key that is not a string stays reachable by its string form")
    fun assertNonStringMapKey() {
        resolves("a.1", "\"one\"", mapOf("a" to mapOf(1 to "one")))
    }

    @Test
    @DisplayName("a present key holding null resolves to null instead of throwing")
    fun assertPresentNullValue() {
        resolves("a", null, mapOf("a" to null))
        resolves("value", null, Holder(value = null))
    }

    @Test
    @DisplayName("a missing key throws")
    fun assertMissingKey() {
        absent("a", mapOf("b" to 1))
        absent("missing", Holder(value = "x"))
    }

    @Test
    @DisplayName("a path continuing past a value or a null throws")
    fun assertPathPastLeaf() {
        absent("a.length", mapOf("a" to "text"))
        absent("value.length", Holder(value = "text"))
        absent("a.b", mapOf("a" to null))
        absent("nested.value", Holder(value = "x"))
    }

    @Test
    @DisplayName("a named step under a collection or an array throws")
    fun assertNamedStepUnderCollection() {
        absent("a.size", mapOf("a" to listOf(1, 2)))
        absent("a.size", mapOf("a" to arrayOf(1, 2)))
    }

    @Test
    @DisplayName("indexes lists, arrays, sets and nested lists")
    fun assertIndexedPaths() {
        resolves("a[0]", "\"first\"", mapOf("a" to listOf("first", "second")))
        resolves("a[1]", "\"second\"", mapOf("a" to arrayOf("first", "second")))
        resolves("a[0]", "\"only\"", mapOf("a" to setOf("only")))
        resolves("a[0][1]", "\"inner\"", mapOf("a" to listOf(listOf("outer", "inner"))))
        resolves("a[0].value", "\"deep\"", mapOf("a" to listOf(Holder(value = "deep"))))
    }

    @Test
    @DisplayName("an out of range, malformed or unsupported index throws")
    fun assertInvalidIndex() {
        absent("a[2]", mapOf("a" to listOf(1, 2)))
        absent("a[2]", mapOf("a" to arrayOf(1, 2)))
        absent("a[x]", mapOf("a" to listOf(1, 2)))
        absent("a[0", mapOf("a" to listOf(1, 2)))
        absent("a[0]", mapOf("a" to "text"))
    }

    @Test
    @DisplayName("the empty path addresses the root only when the root is a value, collection or array")
    fun assertRootPath() {
        assertThat(evaluator.evaluate("" equalsTo listOf(1), listOf(1)), equalTo(true))
        resolves("[0]", 1, listOf(1))
        resolves("[0]", 1, arrayOf(1))
        absent("", mapOf("a" to 1))
        absent("", Holder(value = "x"))
    }

    @Test
    @DisplayName("a quoted literal is a literal even when it spans lines or is empty")
    fun assertQuotedLiteral() {
        resolves("\"a\nb\"", "\"a\nb\"", mapOf("a" to 1))
        resolves("\"\"", "\"\"", mapOf("a" to 1))
        // A lone quote is not a literal, so it stays a path, and no path is named that way here.
        absent("\"", mapOf("a" to 1))
    }

    @Test
    @DisplayName("cached reflection resolves the same path across evaluations")
    fun assertRepeatedReflectionResolution() {
        val input = Holder(value = "x", nested = Holder(value = "y"))

        repeat(2) { resolves("nested.value", "\"y\"", input) }
    }
}
