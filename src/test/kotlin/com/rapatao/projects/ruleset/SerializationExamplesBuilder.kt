package com.rapatao.projects.ruleset

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.allMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.anyMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.noneMatch
import com.rapatao.projects.ruleset.engine.types.builder.equalsTo
import com.rapatao.projects.ruleset.engine.types.builder.greaterOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.greaterThan
import com.rapatao.projects.ruleset.engine.types.builder.isFalse
import com.rapatao.projects.ruleset.engine.types.builder.isTrue
import com.rapatao.projects.ruleset.engine.types.builder.lessOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.lessThan
import com.rapatao.projects.ruleset.jackson.ExpressionMixin
import org.junit.jupiter.api.Test

// @Disabled
internal class SerializationExamplesBuilder {

    private val mapper = jacksonObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .addMixIn(Expression::class.java, ExpressionMixin::class.java)

    private val cases = listOf(
        "field".isTrue(),
        "field".isFalse(),
        "field" equalsTo 10,
        "field" equalsTo "\"value\"",
        "field" equalsTo "value",
        "field" greaterThan 10,
        "field" greaterOrEqualThan 10,
        "field" lessThan 10,
        "field" lessOrEqualThan 10,

        allMatch(
            "field".isTrue(),
            "price" lessThan 10.0,
        ),
        anyMatch(
            "field".isTrue(),
            "price" lessThan 10.0,
        ),

        noneMatch(
            "field".isTrue(),
            "price" lessThan 10.0,
        ),

        Expression(
            allMatch = listOf(
                "field".isTrue(),
                "price" lessThan 10.0,
            ),
            anyMatch = listOf(
                "field".isTrue(),
                "price" lessThan 10.0,
            ),
            noneMatch = listOf(
                "field".isTrue(),
                "price" lessThan 10.0,
            )
        )
    )

    @Test
    fun print() {
        cases.forEach {
            println("```json")
            println(mapper.writeValueAsString(it))
            println("```")
        }
    }
}
