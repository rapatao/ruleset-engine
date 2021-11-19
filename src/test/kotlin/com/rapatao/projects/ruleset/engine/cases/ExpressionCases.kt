package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.types.BetweenExpression
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder
import org.junit.jupiter.params.provider.Arguments
import java.math.BigDecimal

object ExpressionCases {

    fun cases(): List<Arguments> = listOf(
        Arguments.of(
            MatcherBuilder.expression("item.price <= 1000"),
            true
        ),
        Arguments.of(
            MatcherBuilder.expression("item.price >= 1000"),
            false
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.price") between 1 and 1000
            ),
            true
        ),
        Arguments.of(
            MatcherBuilder.expression(
                BetweenExpression("item.price", 1, 1000)
            ),
            true
        ),
        Arguments.of(
            MatcherBuilder.expression(
                BetweenExpression("item.price", 100, 1000)
            ),
            false
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.price") equalsTo BigDecimal.TEN
            ),
            true
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.price") equalsTo BigDecimal.ZERO
            ),
            false
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.trueValue").isTrue()
            ),
            true
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.trueValue").isFalse()
            ),
            false
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.falseValue").isTrue()
            ),
            false
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.falseValue").isFalse()
            ),
            true
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.price") greaterThan BigDecimal.ZERO
            ),
            true
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.price") greaterThan BigDecimal.TEN
            ),
            false
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.price") greaterOrEqualThan BigDecimal.TEN
            ),
            true
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.price") greaterOrEqualThan BigDecimal.valueOf(100)
            ),
            false
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.price") lessThan BigDecimal.valueOf(100)
            ),
            true
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.price") lessThan BigDecimal.ONE
            ),
            false
        ),
        // 43
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.price") lessOrEqualThan BigDecimal.valueOf(100)
            ),
            true
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.price") lessOrEqualThan BigDecimal.TEN
            ),
            true
        ),
        Arguments.of(
            MatcherBuilder.expression(
                ExpressionBuilder.field("item.price") lessOrEqualThan BigDecimal.ONE
            ),
            false
        ),
    )
}
