package com.rapatao.projects.ruleset.engine.evaluator.graaljs

import com.rapatao.projects.ruleset.engine.BaseEvaluatorTest
import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.helper.ExposeEngineTestOperator
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.allMatch
import com.rapatao.projects.ruleset.engine.types.builder.extensions.equalsTo
import com.rapatao.projects.ruleset.engine.types.operators.Operator
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class GraalJSReusedContextEvaluatorTest : BaseEvaluatorTest(evaluator) {

    companion object {
        private const val THREADS = 8

        private val evaluator = GraalJSEvaluator(
            operators = listOf(
                ExposeEngineTestOperator(),
            ),
            reuseContextPerThread = true,
        )
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
    @DisplayName("should restore the input of the outer evaluation when an operator evaluates another expression")
    fun assertNestedEvaluationDoesNotReplaceTheInput() {
        val nesting = NestedEvaluationOperator()
        val evaluator = GraalJSEvaluator(operators = listOf(nesting), reuseContextPerThread = true)

        val rule = allMatch(
            Expression(left = "outer", operator = nesting.name(), right = "1"),
            "outer" equalsTo 1,
        )

        assertThat(evaluator.evaluate(rule, mapOf("outer" to 1)), equalTo(true))
    }

    /**
     * Evaluates an unrelated expression against a different input, the way a custom operator may do through
     * [com.rapatao.projects.ruleset.engine.context.EvalContext.engine].
     */
    private class NestedEvaluationOperator : Operator {
        override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
            context.engine().evaluate("inner" equalsTo 2, mapOf("inner" to 2))

        override fun name(): String = "nested_evaluation"
    }

    @Test
    @DisplayName("should evaluate concurrently without sharing the context between threads")
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
