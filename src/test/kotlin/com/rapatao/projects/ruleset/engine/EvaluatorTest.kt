package com.rapatao.projects.ruleset.engine

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rapatao.projects.ruleset.engine.cases.Scenarios
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Matcher
import com.rapatao.projects.ruleset.jackson.ExpressionMixin
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class EvaluatorTest {

    private val evaluator = Evaluator()
    private val mapper = jacksonObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .addMixIn(Expression::class.java, ExpressionMixin::class.java)

    companion object : Scenarios() {
        @JvmStatic
        fun tests() = super.scenarios()
    }

    private fun doEvaluationTest(ruleSet: Matcher, expected: Boolean) {
        MatcherAssert.assertThat(
            evaluator.evaluate(rule = ruleSet, inputData = inputData),
            Matchers.equalTo(expected)
        )
    }

    private fun compareMatcherList(source: List<Matcher>?, target: List<Matcher>?) {
        MatcherAssert.assertThat(source?.size, Matchers.equalTo(target?.size))

        source?.forEachIndexed { index, matcher ->
            compareMatcher(matcher, target!![index])
        }
    }

    private fun compareMatcher(source: Matcher, target: Matcher) {
        if (target.expression != null) {
            MatcherAssert.assertThat(target.expression, Matchers.instanceOf(source.expression!!.javaClass))
        }

        compareMatcherList(source.allMatch, target.allMatch)
        compareMatcherList(source.anyMatch, target.anyMatch)
        compareMatcherList(source.noneMatch, target.noneMatch)
    }

    private fun doSerializationTest(matcher: Matcher, expected: Boolean) {
        val json = mapper.writeValueAsString(matcher)
        println(json)

        val matcherFromJson = mapper.readValue<Matcher>(json)
        println(matcherFromJson)

        compareMatcher(matcher, matcherFromJson)

        doEvaluationTest(matcherFromJson, expected)
    }

    @ParameterizedTest
    @MethodSource("tests")
    fun `should convert json`(ruleSet: Matcher, expected: Boolean) {
        println(ruleSet)

        doEvaluationTest(ruleSet, expected)

        doSerializationTest(ruleSet, expected)
    }

    @Test
    fun `runs the last test scenario`() {
        val caseNumber = tests().size
        // val caseNumber = 6

        val cases: List<Arguments> = tests()
        val test = cases[caseNumber - 1].get()
        `should convert json`(
            test[0] as Matcher,
            test[1] as Boolean
        )
    }
}
