package com.rapatao.projects.ruleset

import com.rapatao.projects.ruleset.engine.MatcherBuilder.expression
import com.rapatao.projects.ruleset.engine.expressions.ExpressionBuilder.field
import com.rapatao.projects.ruleset.engine.expressions.types.IsBetween
import org.junit.jupiter.params.provider.Arguments
import java.math.BigDecimal

object ExpressionCases {

    fun cases(): List<Arguments> = listOf(
        Arguments.of(
            expression("item.price <= 1000"),
            true
        ),
        Arguments.of(
            expression("item.price >= 1000"),
            false
        ),
        Arguments.of(
            expression(
                field("item.price") between 1 and 1000
            ),
            true
        ),
        Arguments.of(
            expression(
                IsBetween("item.price", 1, 1000)
            ),
            true
        ),
        Arguments.of(
            expression(
                IsBetween("item.price", 100, 1000)
            ),
            false
        ),
        Arguments.of(
            expression(
                field("item.price") equalsTo BigDecimal.TEN
            ),
            true
        ),
        Arguments.of(
            expression(
                field("item.price") equalsTo BigDecimal.ZERO
            ),
            false
        ),
        Arguments.of(
            expression(
                field("item.trueValue").isTrue()
            ),
            true
        ),
        Arguments.of(
            expression(
                field("item.trueValue").isFalse()
            ),
            false
        ),
        Arguments.of(
            expression(
                field("item.falseValue").isTrue()
            ),
            false
        ),
        Arguments.of(
            expression(
                field("item.falseValue").isFalse()
            ),
            true
        ),
        Arguments.of(
            expression(
                field("item.price") greaterThan BigDecimal.ZERO
            ),
            true
        ),
        Arguments.of(
            expression(
                field("item.price") greaterThan BigDecimal.TEN
            ),
            false
        ),
        Arguments.of(
            expression(
                field("item.price") greaterOrEqualThan BigDecimal.TEN
            ),
            true
        ),
        Arguments.of(
            expression(
                field("item.price") greaterOrEqualThan BigDecimal.valueOf(100)
            ),
            false
        ),
        Arguments.of(
            expression(
                field("item.price") lessThan BigDecimal.valueOf(100)
            ),
            true
        ),
        Arguments.of(
            expression(
                field("item.price") lessThan BigDecimal.ONE
            ),
            false
        ),
        // 43
        Arguments.of(
            expression(
                field("item.price") lessOrEqualThan BigDecimal.valueOf(100)
            ),
            true
        ),
        Arguments.of(
            expression(
                field("item.price") lessOrEqualThan BigDecimal.TEN
            ),
            true
        ),
        Arguments.of(
            expression(
                field("item.price") lessOrEqualThan BigDecimal.ONE
            ),
            false
        ),
    )
}
