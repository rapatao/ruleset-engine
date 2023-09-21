package com.rapatao.projects.ruleset

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Matcher
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.isTrue
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.left
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.allMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.anyMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.expression
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.noneMatch
import com.rapatao.projects.ruleset.jackson.ExpressionMixin
import org.junit.jupiter.api.Test

// @Disabled
internal class SerializationExamplesBuilder {

    private val mapper = jacksonObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .addMixIn(Expression::class.java, ExpressionMixin::class.java)

    private val cases = listOf(
        expression(left("field").isTrue()),
        expression(left("field") equalsTo 10),
        expression(left("field") equalsTo "\"value\""),
        expression(left("field") equalsTo "value"),
        expression(left("field") between 10 and 20),
        expression(left("field") greaterThan 10),
        expression(left("field") greaterOrEqualThan 10),
        expression(left("field") lessThan 10),
        expression(left("field") lessOrEqualThan 10),

        allMatch(
            expression(isTrue("field")),
            expression(left("price").lessThan(10.0)),
        ),
        anyMatch(
            expression(isTrue("field")),
            expression(left("price").lessThan(10.0)),
        ),
        noneMatch(
            expression(isTrue("field")),
            expression(left("price").lessThan(10.0)),
        ),
        Matcher(
            allMatch = listOf(
                expression(isTrue("field")),
                expression(left("price").lessThan(10.0)),
            ),
            anyMatch = listOf(
                expression(isTrue("field")),
                expression(left("price").lessThan(10.0)),
            ),
            noneMatch = listOf(
                expression(isTrue("field")),
                expression(left("price").lessThan(10.0)),
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
