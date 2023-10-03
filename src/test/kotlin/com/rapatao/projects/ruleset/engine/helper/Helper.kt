package com.rapatao.projects.ruleset.engine.helper

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rapatao.projects.ruleset.engine.Evaluator
import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.jackson.ExpressionMixin
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo

object Helper {

    @JvmStatic
    val mapper: ObjectMapper = jacksonObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .addMixIn(Expression::class.java, ExpressionMixin::class.java)

    val evaluator = Evaluator()

    fun doEvaluationTest(ruleSet: Expression, expected: Boolean) {
        assertThat(
            evaluator.evaluate(rule = ruleSet, inputData = TestData.inputData),
            equalTo(expected)
        )
    }

    fun compareMatcher(source: Expression, target: Expression) {
        target.parse().run {
            assertThat(this, equalTo(source.parse()))
            assertThat(target.onFailure, equalTo(source.onFailure))

        }

        compareMatcherList(source.allMatch, target.allMatch)
        compareMatcherList(source.anyMatch, target.anyMatch)
        compareMatcherList(source.noneMatch, target.noneMatch)
    }

    private fun compareMatcherList(source: List<Expression>?, target: List<Expression>?) {
        assertThat(source?.size, equalTo(target?.size))

        source?.forEachIndexed { index, matcher ->
            compareMatcher(matcher, target!![index])
        }
    }
}
