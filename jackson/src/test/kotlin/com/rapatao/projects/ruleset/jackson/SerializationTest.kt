package com.rapatao.projects.ruleset.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.KotlinEvaluator
import com.rapatao.projects.ruleset.engine.helper.Helper.compareMatcher
import com.rapatao.projects.ruleset.engine.helper.Helper.doEvaluationTest
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure.THROW
import com.rapatao.projects.ruleset.engine.types.OnFailure.TRUE
import com.rapatao.projects.ruleset.engine.types.builder.extensions.equalsTo
import com.rapatao.projects.ruleset.engine.types.builder.extensions.ifFail
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class SerializationTest {

    private val mapper = jacksonObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .addMixIn(Expression::class.java, ExpressionMixin::class.java)

    companion object {
        @JvmStatic
        fun tests() = TestData.cases()
    }

    @ParameterizedTest
    @MethodSource("tests")
    fun doSerializationTest(matcher: Expression, expected: Boolean) {
        val json = mapper.writeValueAsString(matcher)

        val matcherFromJson = mapper.readValue<Expression>(json)

        compareMatcher(matcher, matcherFromJson)

        doEvaluationTest(KotlinEvaluator(), matcherFromJson, expected)
    }

    @Test
    fun `should set onFailure to THROW when field is not present in the JSON`() {
        val json = """
            {
                "left": "field",
                "operator": "EQUALS",
                "right": 10
            }
        """.trimIndent()

        val matcherFromJson = mapper.readValue<Expression>(json)
        val matcher = "field" equalsTo 10 ifFail THROW

        compareMatcher(matcher, matcherFromJson)
    }

    @Test
    fun `should operator and onFailure fields be case insensitive`() {
        val json = """
            {
                "left": "field",
                "operator": "equals",
                "right": 10,
                "onFailure": "true"
            }
        """.trimIndent()

        val matcherFromJson = mapper.readValue<Expression>(json)
        val matcher = "field" equalsTo 10 ifFail TRUE

        compareMatcher(matcher, matcherFromJson)
    }
}
