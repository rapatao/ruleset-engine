package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.left
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.expression
import com.rapatao.projects.ruleset.engine.types.builder.asExpression
import com.rapatao.projects.ruleset.engine.types.builder.equalsTo
import com.rapatao.projects.ruleset.engine.types.builder.greaterOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.greaterThan
import com.rapatao.projects.ruleset.engine.types.builder.isFalse
import com.rapatao.projects.ruleset.engine.types.builder.isTrue
import com.rapatao.projects.ruleset.engine.types.builder.lessOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.lessThan
import com.rapatao.projects.ruleset.engine.types.builder.onFailure
import org.junit.jupiter.params.provider.Arguments
import java.math.BigDecimal

object ExpressionCases {

    @Suppress("LongMethod")
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
                left("item.price") equalsTo BigDecimal.TEN
            ),
            true
        ),
        Arguments.of(
            expression(
                left("item.price") equalsTo BigDecimal.ZERO
            ),
            false
        ),
        Arguments.of(
            expression(
                left("item.trueValue").isTrue()
            ),
            true
        ),
        Arguments.of(
            expression(
                left("item.trueValue").isFalse()
            ),
            false
        ),
        Arguments.of(
            expression(
                left("item.falseValue").isTrue()
            ),
            false
        ),
        Arguments.of(
            expression(
                left("item.falseValue").isFalse()
            ),
            true
        ),
        Arguments.of(
            expression(
                left("item.price") greaterThan BigDecimal.ZERO
            ),
            true
        ),
        Arguments.of(
            expression(
                left("item.price") greaterThan BigDecimal.TEN
            ),
            false
        ),
        Arguments.of(
            expression(
                left("item.price") greaterOrEqualThan BigDecimal.TEN
            ),
            true
        ),
        Arguments.of(
            expression(
                left("item.price") greaterOrEqualThan BigDecimal.valueOf(100)
            ),
            false
        ),
        Arguments.of(
            expression(
                left("item.price") lessThan BigDecimal.valueOf(100)
            ),
            true
        ),
        Arguments.of(
            expression(
                left("item.price") lessThan BigDecimal.ONE
            ),
            false
        ),
        Arguments.of(
            expression(
                left("item.price") lessOrEqualThan BigDecimal.valueOf(100)
            ),
            true
        ),
        Arguments.of(
            expression(
                left("item.price") lessOrEqualThan BigDecimal.TEN
            ),
            true
        ),
        Arguments.of(
            expression(
                left("item.price") lessOrEqualThan BigDecimal.ONE
            ),
            false
        ),
        Arguments.of(
            expression(
                "item.price" equalsTo BigDecimal.ONE
            ),
            false
        ),
        Arguments.of(
            expression(
                "item.price" equalsTo BigDecimal.TEN
            ),
            true
        ),
        Arguments.of(
            expression(
                "item.falseValue".isTrue()
            ),
            false
        ),
        Arguments.of(
            expression(
                "item.falseValue".isFalse()
            ),
            true
        ),
        Arguments.of(
            expression(
                true.isFalse()
            ),
            false
        ),
        Arguments.of(
            expression(
                false.isFalse()
            ),
            true
        ),
        Arguments.of(
            expression(
                true.isTrue()
            ),
            true
        ),
        Arguments.of(
            expression(
                false.isTrue()
            ),
            false
        ),
        Arguments.of(
            expression(
                "item.price" greaterThan BigDecimal.ZERO
            ),
            true
        ),
        Arguments.of(
            expression(
                "item.price" greaterThan BigDecimal.TEN
            ),
            false
        ),
        Arguments.of(
            expression(
                "item.price" greaterOrEqualThan BigDecimal.TEN
            ),
            true
        ),
        Arguments.of(
            expression(
                "item.price" greaterOrEqualThan BigDecimal.valueOf(100)
            ),
            false
        ),
        Arguments.of(
            expression(
                "item.price" lessOrEqualThan BigDecimal.valueOf(100)
            ),
            true
        ),
        Arguments.of(
            expression(
                "item.price" lessOrEqualThan BigDecimal.TEN
            ),
            true
        ),
        Arguments.of(
            expression(
                "item.price" lessOrEqualThan BigDecimal.ONE
            ),
            false
        ),
        Arguments.of(
            expression(
                "item.price" lessThan BigDecimal.valueOf(100)
            ),
            true
        ),
        Arguments.of(
            expression(
                "item.price" lessThan BigDecimal.ONE
            ),
            false
        ),
        Arguments.of(
            expression(
                "item.price <= 1000".asExpression(),
            ),
            true
        ),
        Arguments.of(
            expression(
                "item.price >= 1000".asExpression(),
            ),
            false
        ),
        Arguments.of(
            expression(
                "item.field.that.dont.exist" equalsTo "10" onFailure OnFailure.TRUE
            ),
            true
        ),
        Arguments.of(
            expression(
                "item.field.that.dont.exist" equalsTo "10" onFailure OnFailure.FALSE
            ),
            false
        ),
    )
}
