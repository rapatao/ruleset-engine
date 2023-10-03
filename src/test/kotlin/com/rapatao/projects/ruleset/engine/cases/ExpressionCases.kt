package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.builder.asExpression
import com.rapatao.projects.ruleset.engine.types.builder.equalsTo
import com.rapatao.projects.ruleset.engine.types.builder.greaterOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.greaterThan
import com.rapatao.projects.ruleset.engine.types.builder.ifFail
import com.rapatao.projects.ruleset.engine.types.builder.isFalse
import com.rapatao.projects.ruleset.engine.types.builder.isTrue
import com.rapatao.projects.ruleset.engine.types.builder.lessOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.lessThan
import org.junit.jupiter.params.provider.Arguments

object ExpressionCases {

    @Suppress("LongMethod")
    fun cases(): List<Arguments> = listOf(
        Arguments.of(
            "item.price <= 1000".asExpression(),
            true
        ),
        Arguments.of(
            "item.price >= 1000".asExpression(),
            false
        ),
        Arguments.of(
            "item.price" equalsTo 10,
            true
        ),
        Arguments.of(
            "item.price" equalsTo 0,
            false
        ),
        Arguments.of(
            "item.trueValue".isTrue(),
            true
        ),
        Arguments.of(
            "item.trueValue".isFalse(),
            false
        ),
        Arguments.of(
            "item.falseValue".isTrue(),
            false
        ),
        Arguments.of(
            "item.falseValue".isFalse(),
            true
        ),
        Arguments.of(
            "item.price" greaterThan 0,
            true
        ),
        Arguments.of(
            "item.price" greaterThan 10,
            false
        ),
        Arguments.of(
            "item.price" greaterOrEqualThan 10,
            true
        ),
        Arguments.of(
            "item.price" greaterOrEqualThan 100,
            false
        ),
        Arguments.of(
            "item.price" lessThan 100,
            true
        ),
        Arguments.of(
            "item.price" lessThan 1,
            false
        ),
        Arguments.of(
            "item.price" lessOrEqualThan 100,
            true
        ),
        Arguments.of(
            "item.price" lessOrEqualThan 10,
            true
        ),
        Arguments.of(
            "item.price" lessOrEqualThan 1,
            false
        ),
        Arguments.of(
            "item.price" equalsTo 1,
            false
        ),
        Arguments.of(
            "item.price" equalsTo 10,
            true
        ),
        Arguments.of(
            "item.falseValue".isTrue(),
            false,
        ),
        Arguments.of(
            "item.falseValue".isFalse(),
            true
        ),
        Arguments.of(
            true.isFalse(),
            false
        ),
        Arguments.of(
            false.isFalse(),
            true
        ),
        Arguments.of(
            true.isTrue(),
            true
        ),
        Arguments.of(
            false.isTrue(),
            false
        ),
        Arguments.of(
            "item.price" greaterThan 0,
            true
        ),
        Arguments.of(
            "item.price" greaterThan 10,
            false
        ),
        Arguments.of(
            "item.price" greaterOrEqualThan 10,
            true
        ),
        Arguments.of(
            "item.price" greaterOrEqualThan 100,
            false
        ),
        Arguments.of(
            "item.price" lessOrEqualThan 100,
            true
        ),
        Arguments.of(
            "item.price" lessOrEqualThan 10,
            true
        ),
        Arguments.of(
            "item.price" lessOrEqualThan 1,
            false
        ),
        Arguments.of(
            "item.price" lessThan 100,
            true
        ),
        Arguments.of(
            "item.price" lessThan 1,
            false
        ),
        Arguments.of(
            "item.price <= 1000".asExpression(),
            true
        ),
        Arguments.of(
            "item.price >= 1000".asExpression(),
            false
        ),
        Arguments.of(
            "item.field.that.dont.exist" equalsTo "10" ifFail OnFailure.TRUE,
            true
        ),
        Arguments.of(
            "item.field.that.dont.exist" equalsTo "10" ifFail OnFailure.FALSE,
            false
        ),
    )
}
