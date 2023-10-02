package com.rapatao.projects.ruleset.engine.helper

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Matcher
import com.rapatao.projects.ruleset.jackson.ExpressionMixin
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.instanceOf

object Helper {

    @JvmStatic
    val mapper: ObjectMapper = jacksonObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .addMixIn(Expression::class.java, ExpressionMixin::class.java)

    fun compareMatcher(source: Matcher, target: Matcher) {
        target.expression?.run {
            assertThat(this, instanceOf(source.expression!!.javaClass))
            assertThat(this.parse(), Matchers.equalTo(source.expression?.parse()))
            assertThat(this.onFailure(), Matchers.equalTo(source.expression?.onFailure()))

        }

        compareMatcherList(source.allMatch, target.allMatch)
        compareMatcherList(source.anyMatch, target.anyMatch)
        compareMatcherList(source.noneMatch, target.noneMatch)
    }

    private fun compareMatcherList(source: List<Matcher>?, target: List<Matcher>?) {
        assertThat(source?.size, Matchers.equalTo(target?.size))

        source?.forEachIndexed { index, matcher ->
            compareMatcher(matcher, target!![index])
        }
    }
}
