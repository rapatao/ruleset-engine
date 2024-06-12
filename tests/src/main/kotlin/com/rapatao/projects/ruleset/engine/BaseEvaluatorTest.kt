package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.helper.Helper.doEvaluationTest
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.allMatch
import com.rapatao.projects.ruleset.engine.types.builder.extensions.equalsTo
import com.rapatao.projects.ruleset.engine.types.builder.extensions.ifFail
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.emptyString
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

abstract class BaseEvaluatorTest(
    private val evaluator: Evaluator
) {

    companion object {

        @JvmStatic
        fun tests() = TestData.allCases()

        @JvmStatic
        fun onFailure() = TestData.onFailureCases()
    }

    @ParameterizedTest
    @MethodSource("tests")
    fun runEvaluationTest(ruleSet: Expression, expected: Boolean) {
        println(ruleSet)

        doEvaluationTest(evaluator, ruleSet, expected)
    }

    @ParameterizedTest
    @MethodSource("onFailure")
    fun asserOnFailure(ruleSet: Expression, expected: Boolean, isError: Boolean) {
        println(ruleSet)

        if (isError) {
            assertThrows<Exception> {
                doEvaluationTest(evaluator, ruleSet, expected)
            }
        } else {
            doEvaluationTest(evaluator, ruleSet, expected)
        }
    }

    @Test
    @Suppress("MagicNumber")
    fun assertSingleCaseForDebugging() {
        val caseNumber = 122

        val cases: List<Arguments> = tests()
        val test = cases[caseNumber - 1].get()
        runEvaluationTest(
            test[0] as Expression,
            test[1] as Boolean
        )
    }

    @Test
    @DisplayName("should support map as input data")
    fun assertMapAsInputDataSupport() {
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

        val result = evaluator.evaluate(rule, input)

        assertThat(result, equalTo(true))
    }

    @Test
    @DisplayName("should throw exception when failure was not defined")
    @Suppress("MagicNumber")
    fun assertThrowExceptionWhenFailureIsNotDefined() {
        val invalidRule = "[]unkown$" equalsTo 10
        val input = mapOf<String, Any>()

        assertThrows<Exception> {
            evaluator.evaluate(invalidRule, input)
        }
    }

    @Test
    @DisplayName("should override value when failure was defined")
    @Suppress("MagicNumber")
    fun assertOverrideValueWhenFailureIsDefined() {
        val invalidRule = "[]unkown$" equalsTo 10
        val input = mapOf<String, Any>()

        assertThat(
            evaluator.evaluate(invalidRule ifFail OnFailure.TRUE, input),
            equalTo(true)
        )

        assertThat(
            evaluator.evaluate(invalidRule ifFail OnFailure.FALSE, input),
            equalTo(false)
        )
    }

    @Test
    @DisplayName("evaluator must have a non empty name")
    fun assertEvaluatorMustHaveName() {
        assertThat(evaluator.name(), not(emptyString()))
    }
}
