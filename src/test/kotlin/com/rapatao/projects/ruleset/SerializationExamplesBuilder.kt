package com.rapatao.projects.ruleset

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.isTrue
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.left
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.allMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.anyMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.noneMatch
import com.rapatao.projects.ruleset.jackson.ExpressionMixin
import org.junit.jupiter.api.Test

// @Disabled
internal class SerializationExamplesBuilder {

    private val mapper = jacksonObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .addMixIn(Expression::class.java, ExpressionMixin::class.java)

    private val cases = listOf(
        left("field").isTrue(),
        left("field") equalsTo 10,
        left(10) equalsTo 10,
        left("field") equalsTo "\"value\"",
        left("field") equalsTo "value",
        left("field") greaterThan 10,
        left("field") greaterOrEqualThan 10,
        left("field") lessThan 10,
        left("field") lessOrEqualThan 10,

        allMatch(
            isTrue("field"),
            left("price").lessThan(10.0),
        ),
        anyMatch(
            isTrue("field"),
            left("price").lessThan(10.0),
        ),
        noneMatch(
            isTrue("field"),
            left("price").lessThan(10.0),
        ),
        Expression(
            allMatch = listOf(
                isTrue("field"),
                left("price").lessThan(10.0),
            ),
            anyMatch = listOf(
                isTrue("field"),
                left("price").lessThan(10.0),
            ),
            noneMatch = listOf(
                isTrue("field"),
                left("price").lessThan(10.0),
            ),
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
