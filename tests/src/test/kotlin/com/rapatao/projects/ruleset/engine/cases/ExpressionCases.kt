package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.builder.extensions.equalsTo
import com.rapatao.projects.ruleset.engine.types.builder.extensions.from
import com.rapatao.projects.ruleset.engine.types.builder.extensions.fromInclusive
import com.rapatao.projects.ruleset.engine.types.builder.extensions.greaterOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.greaterThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.ifFail
import com.rapatao.projects.ruleset.engine.types.builder.extensions.isFalse
import com.rapatao.projects.ruleset.engine.types.builder.extensions.isTrue
import com.rapatao.projects.ruleset.engine.types.builder.extensions.lessOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.lessThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.notEqualsTo
import org.junit.jupiter.params.provider.Arguments

object ExpressionCases {

    @Suppress("LongMethod")
    fun cases(): List<Arguments> =
        equalsCases() +
            notEqualsCases() +
            booleanCases() +
            ifFailCases() +
            betweenCases() +
            greaterThanCases() +
            lessThanCases() +
            lessOrEqualThanCases() +
            greaterOrEqualThanCases()

    private fun greaterOrEqualThanCases() = listOf(
        Arguments.of(
            "item.price" greaterOrEqualThan 1,
            true
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
            10 greaterOrEqualThan 1,
            true
        ),
        Arguments.of(
            10 greaterOrEqualThan 10,
            true
        ),
        Arguments.of(
            10 greaterOrEqualThan 100,
            false
        ),
    )

    private fun lessOrEqualThanCases() = listOf(
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
            10 lessOrEqualThan 100,
            true
        ),
        Arguments.of(
            10 lessOrEqualThan 10,
            true
        ),
        Arguments.of(
            10 lessOrEqualThan 1,
            false
        ),
    )

    private fun lessThanCases() = listOf(
        Arguments.of(
            "item.price" lessThan 100,
            true
        ),
        Arguments.of(
            "item.price" lessThan 1,
            false
        ),
        Arguments.of(
            "item.price" lessThan 10,
            false
        ),
        Arguments.of(
            2 lessThan 10,
            true
        ),
        Arguments.of(
            20 lessThan 10,
            false
        ),
        Arguments.of(
            10 lessThan 10,
            false
        ),
    )

    private fun greaterThanCases() = listOf(
        Arguments.of(
            "item.price" greaterThan 0,
            true
        ),
        Arguments.of(
            "item.price" greaterThan 10,
            false
        ),
        Arguments.of(
            "item.price" greaterThan 20,
            false
        ),
        Arguments.of(
            10 greaterThan 20,
            false
        ),
        Arguments.of(
            30 greaterThan 20,
            true
        ),
    )

    private fun equalsCases() = listOf(
        Arguments.of(
            "item.price" equalsTo 10,
            true
        ),
        Arguments.of(
            "item.price" equalsTo 0,
            false
        ),
    )

    private fun notEqualsCases() = listOf(
        Arguments.of(
            "item.price" notEqualsTo 0,
            true
        ),
        Arguments.of(
            "item.price" notEqualsTo 10,
            false
        ),
    )

    private fun booleanCases() = listOf(
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
            "item.trueValue" equalsTo "true",
            true
        ),
        Arguments.of(
            "item.trueValue" equalsTo true,
            true
        ),
        Arguments.of(
            "item.trueValue" notEqualsTo "false",
            true
        ),
        Arguments.of(
            "item.trueValue" notEqualsTo false,
            true
        ),
    )

    private fun ifFailCases() = listOf(
        Arguments.of(
            "item.field.that.dont.exist" equalsTo "10" ifFail OnFailure.TRUE,
            true
        ),
        Arguments.of(
            "item.field.that.dont.exist" equalsTo "10" ifFail OnFailure.FALSE,
            false
        ),
    )

    @SuppressWarnings("LongMethod")
    private fun betweenCases() = listOf(
        Arguments.of(
            "item.price" from 5 to 15,
            true
        ),
        Arguments.of(
            "item.price" from 5 toInclusive 15,
            true
        ),
        Arguments.of(
            "item.price" fromInclusive 5 to 15,
            true
        ),
        Arguments.of(
            "item.price" fromInclusive 5 toInclusive 15,
            true
        ),
        Arguments.of(
            "item.price" from 5 to 9,
            false
        ),
        Arguments.of(
            "item.price" from 5 toInclusive 9,
            false
        ),
        Arguments.of(
            "item.price" fromInclusive 5 to 9,
            false
        ),
        Arguments.of(
            "item.price" fromInclusive 5 toInclusive 9,
            false
        ),
        Arguments.of(
            "item.price" from 11 to 15,
            false
        ),
        Arguments.of(
            "item.price" from 11 toInclusive 15,
            false
        ),
        Arguments.of(
            "item.price" fromInclusive 11 to 15,
            false
        ),
        Arguments.of(
            "item.price" fromInclusive 11 toInclusive 15,
            false
        ),
        Arguments.of(
            "item.price" from 10 to 15,
            false
        ),
        Arguments.of(
            "item.price" from 10 toInclusive 15,
            false
        ),
        Arguments.of(
            "item.price" fromInclusive 10 to 15,
            true
        ),
        Arguments.of(
            "item.price" fromInclusive 10 toInclusive 15,
            true
        ),
        Arguments.of(
            "item.price" from 5 to 10,
            false
        ),
        Arguments.of(
            "item.price" from 5 toInclusive 10,
            true
        ),
        Arguments.of(
            "item.price" fromInclusive 5 to 10,
            false
        ),
        Arguments.of(
            "item.price" fromInclusive 5 toInclusive 10,
            true
        ),
    )
}
