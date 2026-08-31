package com.rapatao.projects.ruleset.engine.evaluator.rhino

import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.builder.extensions.equalsTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class RhinoSharedScopeTest {

    companion object {
        private const val THREADS = 8

        private val evaluator = RhinoEvaluator()
    }

    @Test
    @DisplayName("should not expose bindings from a previous evaluation")
    fun assertBindingsAreNotSharedBetweenEvaluations() {
        val rule = "b" equalsTo 2

        assertThat(
            evaluator.evaluate(rule, mapOf("a" to 1, "b" to 2)),
            equalTo(true),
        )

        assertThrows<Exception> {
            evaluator.evaluate(rule, mapOf("a" to 1))
        }
    }

    @Test
    @DisplayName("should not expose globals defined by a previous evaluation")
    fun assertRuleGlobalsAreNotSharedBetweenEvaluations() {
        val defining = "(function() { leaked = 1; return true })()" equalsTo true

        assertThat(evaluator.evaluate(defining, mapOf("a" to 1)), equalTo(true))

        assertThat(
            evaluator.evaluate("typeof leaked" equalsTo "'undefined'", mapOf("a" to 1)),
            equalTo(true),
        )
    }

    @Test
    @DisplayName("should not let an evaluation mutate the shared standard objects")
    fun assertStandardObjectsAreSealed() {
        val mutating = "(function() { Array.prototype.leaked = 1; return true })()" equalsTo true

        assertThrows<Exception> {
            evaluator.evaluate(mutating, mapOf("a" to 1))
        }
    }

    @Test
    @DisplayName("should evaluate concurrently without sharing the scope between threads")
    fun assertConcurrentEvaluation() {
        val cases = TestData.cases().map { it.get() }
            .map { it[0] as Expression to it[1] as Boolean }

        val pool = Executors.newFixedThreadPool(THREADS)
        try {
            val results = (1..THREADS).map {
                pool.submit {
                    cases.forEach { (expression, expected) ->
                        assertThat(evaluator.evaluate(expression, TestData.inputData), equalTo(expected))
                    }
                }
            }

            results.forEach { it.get(1, TimeUnit.MINUTES) }
        } finally {
            pool.shutdownNow()
        }
    }
}
