package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.allMatch
import com.rapatao.projects.ruleset.engine.types.builder.equalsTo
import com.rapatao.projects.ruleset.engine.types.builder.greaterThan
import com.rapatao.projects.ruleset.engine.types.builder.lessThan
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class EvaluatorTest {

    private val evaluator = Evaluator()

    companion object {
        @JvmStatic
        fun tests() = TestData.allCases()
    }

    private fun doEvaluationTest(ruleSet: Expression, expected: Boolean) {
        assertThat(
            evaluator.evaluate(rule = ruleSet, inputData = TestData.inputData),
            equalTo(expected)
        )
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
        val caseNumber = 40

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

    fun test() {
        allMatch(
            "left" greaterThan 10,
            "left" lessThan 20,
        )
    }
}
