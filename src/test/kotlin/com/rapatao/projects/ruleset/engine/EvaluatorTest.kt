package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.helper.Helper.doEvaluationTest
import com.rapatao.projects.ruleset.engine.helper.Helper.evaluator
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.allMatch
import com.rapatao.projects.ruleset.engine.types.builder.equalsTo
import com.rapatao.projects.ruleset.engine.types.builder.ifFail
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
        fun tests() = TestData.allCases()
    }

    @ParameterizedTest
    @MethodSource("tests")
    fun `should convert json`(ruleSet: Expression, expected: Boolean) {
        println(ruleSet)

        doEvaluationTest(ruleSet, expected)
    }

    @Test
    fun `runs the last test scenario`() {
        // val caseNumber = tests().size
        val caseNumber = 70

        val cases: List<Arguments> = tests()
        val test = cases[caseNumber - 1].get()
        `should convert json`(
            test[0] as Expression,
            test[1] as Boolean
        )
    }

    @Test
    fun `should support map as input data`() {
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
    fun `should throw exception when failure was not defined`() {
        val invalidRule = "[]unkown$" equalsTo 10
        val input = mapOf<String, Any>()

        assertThrows<Exception> {
            evaluator.evaluate(invalidRule, input)
        }
    }

    @Test
    fun `should override value when failure was defined`() {
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
}
