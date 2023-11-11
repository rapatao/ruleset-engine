package com.rapatao.projects.ruleset.engine.evaluator.rhino

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Operator
import com.rapatao.projects.ruleset.engine.types.builder.equalsTo
import com.rapatao.projects.ruleset.engine.types.builder.greaterOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.greaterThan
import com.rapatao.projects.ruleset.engine.types.builder.lessOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.lessThan
import com.rapatao.projects.ruleset.engine.types.builder.notEqualsTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
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
                "(field) == (10)"
            ),
            Arguments.of(
                ("field" notEqualsTo 10),
                "(field) != (10)"
            ),
            Arguments.of(
                ("field" greaterThan 10),
                "(field) > (10)"
            ),
            Arguments.of(
                ("field" greaterOrEqualThan 10),
                "(field) >= (10)"
            ),
            Arguments.of(
                ("field" lessThan 10),
                "(field) < (10)"
            ),
            Arguments.of(
                ("field" lessOrEqualThan 10),
                "(field) <= (10)"
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("cases")
    fun `parse as expected`(expression: Expression, expected: String) {
        assertThat(Parser.parse(expression), equalTo(expected))
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
            !it.contains("null")
        }.toSet()

        assertThat(parsed, hasSize(values.size))
    }
}
