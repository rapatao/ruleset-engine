package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.types.builder.extensions.equalsTo
import com.rapatao.projects.ruleset.engine.types.builder.extensions.expContains
import com.rapatao.projects.ruleset.engine.types.builder.extensions.expNotContains
import com.rapatao.projects.ruleset.engine.types.builder.extensions.greaterOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.greaterThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.lessThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.notEqualsTo
import org.junit.jupiter.params.provider.Arguments

/**
 * Numbers that are not whole, and numbers inside a list.
 *
 * Every engine has to agree on these: a decimal field compares by its full value, two numbers that differ only in
 * scale are the same number, and a list of numbers is searched by value.
 */
object NumberCases {

    fun cases(): List<Arguments> = fractionCases() + scaleCases() + numberListCases()

    @Suppress("MagicNumber")
    private fun fractionCases(): List<Arguments> = listOf(
        // item.weight is 1.5
        Arguments.of("item.weight" equalsTo "1.5", true),
        Arguments.of("item.weight" equalsTo "1.9", false),
        Arguments.of("item.weight" equalsTo "1.0", false),
        Arguments.of("item.weight" equalsTo "1", false),
        Arguments.of("item.weight" notEqualsTo "1.9", true),
        Arguments.of("item.weight" greaterThan "1.4", true),
        Arguments.of("item.weight" greaterThan "1.6", false),
        Arguments.of("item.weight" lessThan "1.6", true),
        Arguments.of("item.weight" lessThan "1.4", false),
        Arguments.of("item.weight" greaterOrEqualThan "1.5", true),
    )

    @Suppress("MagicNumber")
    private fun scaleCases(): List<Arguments> = listOf(
        // item.scaled is 10.00, and differs from 10 only in scale
        Arguments.of("item.scaled" equalsTo 10, true),
        Arguments.of("item.scaled" equalsTo "10.0", true),
        Arguments.of("item.scaled" equalsTo "10.00", true),
        Arguments.of("item.scaled" notEqualsTo 10, false),
        Arguments.of("item.scaled" equalsTo "10.01", false),
        Arguments.of("item.price" equalsTo "10.00", true),
    )

    @Suppress("MagicNumber")
    private fun numberListCases(): List<Arguments> = listOf(
        // written in the expression
        Arguments.of(listOf(1, 2) expContains 1, true),
        Arguments.of(listOf(1, 2) expContains 3, false),
        Arguments.of(listOf(1, 2) expNotContains 3, true),
        // read from the input data, item.quantities is [1, 2]
        Arguments.of("item.quantities" expContains "1", true),
        Arguments.of("item.quantities" expContains "3", false),
        Arguments.of("item.quantities" expNotContains "3", true),
        Arguments.of("item.quantities" expContains "1.0", true),
    )
}
