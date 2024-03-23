package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.context.EvalEngine
import com.rapatao.projects.ruleset.engine.helper.Helper.doEvaluationTest
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.allMatch
import com.rapatao.projects.ruleset.engine.types.builder.extensions.equalsTo
import com.rapatao.projects.ruleset.engine.types.builder.extensions.ifFail
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class EvaluatorTest {

    companion object {

        @JvmStatic
        fun engines() = TestData.engines()

        @JvmStatic
        fun tests() = TestData.allCases()

        @JvmStatic
        fun onFailure() = TestData.onFailureCases()
    }

    @ParameterizedTest
    @MethodSource("tests")
    fun runEvaluationTest(engine: EvalEngine, ruleSet: Expression, expected: Boolean) {
        println(ruleSet)

        doEvaluationTest(engine, ruleSet, expected)
    }

    @ParameterizedTest
    @MethodSource("onFailure")
    fun `assert onFailure`(engine: EvalEngine, ruleSet: Expression, expected: Boolean, isError: Boolean) {
        println(ruleSet)

        if (isError) {
            assertThrows<Exception> {
                doEvaluationTest(engine, ruleSet, expected)
            }
        } else {
            doEvaluationTest(engine, ruleSet, expected)
        }
    }

    @Test
    fun `run single test case`() {
        val caseNumber = 242

        val cases: List<Arguments> = tests()
        val test = cases[caseNumber - 1].get()
        runEvaluationTest(
            test[0] as EvalEngine,
            test[1] as Expression,
            test[2] as Boolean
        )
    }

    @ParameterizedTest
    @MethodSource("engines")
    fun `should support map as input data`(engine: EvalEngine) {
        val input = mapOf(
            "item" to mapOf(
                "price" to 0,
            ),
            "attributes" to mapOf(
                "info" to mapOf(
                    "title" to "superb title",
                    "description" to "super description",
                ),
            )
        )

        val rule = allMatch(
            "item.price" equalsTo 0,
            "attributes.info.title" equalsTo "\"superb title\"",
            "attributes.info.description" equalsTo "\"super description\"",
        )

        val result = Evaluator(engine = engine).evaluate(rule, input)

        assertThat(result, equalTo(true))
    }

    @ParameterizedTest
    @MethodSource("engines")
    fun `should throw exception when failure was not defined`(engine: EvalEngine) {
        val invalidRule = "[]unkown$" equalsTo 10
        val input = mapOf<String, Any>()

        assertThrows<Exception> {
            Evaluator(engine = engine).evaluate(invalidRule, input)
        }
    }

    @ParameterizedTest
    @MethodSource("engines")
    fun `should override value when failure was defined`(engine: EvalEngine) {
        val invalidRule = "[]unkown$" equalsTo 10
        val input = mapOf<String, Any>()

        assertThat(
            Evaluator(engine = engine).evaluate(invalidRule ifFail OnFailure.TRUE, input),
            equalTo(true)
        )

        assertThat(
            Evaluator(engine = engine).evaluate(invalidRule ifFail OnFailure.FALSE, input),
            equalTo(false)
        )
    }
}
