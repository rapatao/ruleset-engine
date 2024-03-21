package com.rapatao.projects.ruleset.engine.evaluator.rhino

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Operator
import com.rapatao.projects.ruleset.engine.types.builder.extensions.endsWith
import com.rapatao.projects.ruleset.engine.types.builder.extensions.equalsTo
import com.rapatao.projects.ruleset.engine.types.builder.extensions.greaterOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.greaterThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.lessOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.lessThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.notEqualsTo
import com.rapatao.projects.ruleset.engine.types.builder.extensions.startsWith
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.emptyString
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class ParserTest {

    companion object {
        @JvmStatic
        fun cases() = listOf(
            Arguments.of(
                ("field" equalsTo 10),
            ),
            Arguments.of(
                ("field" notEqualsTo 10),
            ),
            Arguments.of(
                ("field" greaterThan 10),
            ),
            Arguments.of(
                ("field" greaterOrEqualThan 10),
            ),
            Arguments.of(
                ("field" lessThan 10),
            ),
            Arguments.of(
                ("field" lessOrEqualThan 10),
            ),
            Arguments.of(
                ("field" startsWith 10),
            ),
            Arguments.of(
                ("field" endsWith 10),
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("cases")
    fun `parse as expected`(expression: Expression) {
        assertThat(Parser.parse(expression), not(emptyString()))
    }

    @Test
    fun `all operator is mapped`() {
        val values: Set<Operator> = Operator.entries.toSet()

        val withCases: Set<Operator> = cases().mapNotNull {
            val expression: Expression = it.get().first() as Expression
            expression.operator()
        }.toSet()

        assertThat(values.subtract(withCases), hasSize(0))

        val parsed = values.map {
            Parser.parse(Expression(left = "field", operator = it, right = 10))
        }.filter {
            !it.contains("null") && it.trim().isNotEmpty()
        }.toSet()

        assertThat(parsed, hasSize(values.size))
    }
}
