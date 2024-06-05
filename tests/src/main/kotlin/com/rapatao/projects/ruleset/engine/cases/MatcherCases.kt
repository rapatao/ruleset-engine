package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.allMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.anyMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.noneMatch
import com.rapatao.projects.ruleset.engine.types.builder.extensions.expContains
import com.rapatao.projects.ruleset.engine.types.builder.extensions.greaterOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.lessOrEqualThan
import org.junit.jupiter.params.provider.Arguments

object MatcherCases {

    fun cases(): List<Arguments> = noneMatchCases() + allMatchCases() + anyMatchCases()

    @Suppress("LongMethod")
    private fun noneMatchCases(): List<Arguments> = listOf(
        Arguments.of(
            noneMatch("item.price" greaterOrEqualThan "1000"),
            true
        ),
        Arguments.of(
            noneMatch("item.price" lessOrEqualThan "1000"),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price" greaterOrEqualThan "1000",
                "item.price" greaterOrEqualThan "10000",
            ),
            true
        ),
        Arguments.of(
            noneMatch(
                "item.price" lessOrEqualThan "1000",
                "item.price" greaterOrEqualThan "1000",
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price" greaterOrEqualThan "1000",
                "item.price" lessOrEqualThan "1000",
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price" lessOrEqualThan "1000",
                "item.price" lessOrEqualThan "1000",
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price" greaterOrEqualThan "1000",
                anyMatch("item.price" greaterOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            noneMatch(
                "item.price" greaterOrEqualThan "1000",
                anyMatch("item.price" lessOrEqualThan "1000")
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price" greaterOrEqualThan "1000",
                noneMatch("item.price" greaterOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            noneMatch(
                "item.price" greaterOrEqualThan "1000",
                noneMatch("item.price" lessOrEqualThan "1000")
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price" greaterOrEqualThan "1000",
                allMatch("item.price" greaterOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            noneMatch(
                "item.price" greaterOrEqualThan "1000",
                allMatch("item.price" lessOrEqualThan "1000")
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price" greaterOrEqualThan "1000",
                anyMatch("item.price" greaterOrEqualThan "1000"),
                allMatch("item.price" greaterOrEqualThan "1000"),
                noneMatch("item.price" greaterOrEqualThan "1000"),
            ),
            true
        ),
        Arguments.of(
            noneMatch(
                "item.price" greaterOrEqualThan "1000",
                anyMatch("item.price" lessOrEqualThan "1000"),
                allMatch("item.price" greaterOrEqualThan "1000"),
                noneMatch("item.price" greaterOrEqualThan "1000")
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price" greaterOrEqualThan "1000",
                anyMatch("item.price" greaterOrEqualThan "1000"),
                allMatch("item.price" lessOrEqualThan "1000"),
                noneMatch("item.price" greaterOrEqualThan "1000")
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price" greaterOrEqualThan "1000",
                anyMatch("item.price" greaterOrEqualThan "1000"),
                allMatch("item.price" greaterOrEqualThan "1000"),
                noneMatch("item.price" lessOrEqualThan "1000")
            ),
            false
        ),
    )

    @Suppress("LongMethod")
    private fun allMatchCases(): List<Arguments> = listOf(
        Arguments.of(
            allMatch("item.price" lessOrEqualThan "1000"),
            true
        ),
        Arguments.of(
            allMatch("item.price" greaterOrEqualThan "1000"),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price" lessOrEqualThan "1000",
                "item.price" lessOrEqualThan "1000",
            ),
            true
        ),
        Arguments.of(
            allMatch(
                "item.price" greaterOrEqualThan "1000",
                "item.price" lessOrEqualThan "1000",
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price" lessOrEqualThan "1000",
                "item.price" greaterOrEqualThan "1000",
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price" greaterOrEqualThan "1000",
                "item.price" greaterOrEqualThan "1000",
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price" lessOrEqualThan "1000",
                anyMatch("item.price" lessOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            allMatch(
                "item.price" lessOrEqualThan "1000",
                anyMatch("item.price" greaterOrEqualThan "1000")
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price" lessOrEqualThan "1000",
                noneMatch("item.price" greaterOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            allMatch(
                "item.price" lessOrEqualThan "1000",
                noneMatch("item.price" lessOrEqualThan "1000")
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price" lessOrEqualThan "1000",
                allMatch("item.price" lessOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            allMatch(
                "item.price" lessOrEqualThan "1000",
                allMatch("item.price" greaterOrEqualThan "1000")
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price" lessOrEqualThan "1000",
                anyMatch("item.price" lessOrEqualThan "1000"),
                allMatch("item.price" lessOrEqualThan "1000"),
                noneMatch("item.price" greaterOrEqualThan "1000"),
            ),
            true
        ),
        Arguments.of(
            allMatch(
                "item.price" lessOrEqualThan "1000",
                anyMatch("item.price" greaterOrEqualThan "1000"),
                allMatch("item.price" lessOrEqualThan "1000"),
                noneMatch("item.price" greaterOrEqualThan "1000")
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price" lessOrEqualThan "1000",
                anyMatch("item.price" lessOrEqualThan "1000"),
                allMatch("item.price" greaterOrEqualThan "1000"),
                noneMatch("item.price" greaterOrEqualThan "1000")
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price" lessOrEqualThan "1000",
                anyMatch("item.price" lessOrEqualThan "1000"),
                allMatch("item.price" lessOrEqualThan "1000"),
                noneMatch("item.price" lessOrEqualThan "1000")
            ),
            false
        ),
    )

    @Suppress("LongMethod")
    private fun anyMatchCases(): List<Arguments> = listOf(
        Arguments.of(
            anyMatch("item.price" lessOrEqualThan "1000"),
            true
        ),
        Arguments.of(
            anyMatch("item.price" greaterOrEqualThan "1000"),
            false
        ),
        Arguments.of(
            anyMatch(
                "item.price" lessOrEqualThan "1000",
                "item.price" greaterOrEqualThan "1000"
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price" greaterOrEqualThan "1000",
                "item.price" greaterOrEqualThan "1000"
            ),
            false
        ),
        Arguments.of(
            anyMatch(
                "item.price" lessOrEqualThan "1000",
                anyMatch("item.price" lessOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price" greaterOrEqualThan "1000",
                anyMatch("item.price" lessOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price" lessOrEqualThan "1000",
                anyMatch("item.price" greaterOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price" greaterOrEqualThan "1000",
                anyMatch("item.price" greaterOrEqualThan "1000")
            ),
            false
        ),
        Arguments.of(
            anyMatch(
                "item.price" lessOrEqualThan "1000",
                noneMatch("item.price" greaterOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price" greaterOrEqualThan "1000",
                noneMatch("item.price" greaterOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price" lessOrEqualThan "1000",
                noneMatch("item.price" lessOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price" greaterOrEqualThan "1000",
                noneMatch("item.price" lessOrEqualThan "1000")
            ),
            false
        ),
        Arguments.of(
            anyMatch(
                "item.price" lessOrEqualThan "1000",
                allMatch("item.price" lessOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price" greaterOrEqualThan "1000",
                allMatch("item.price" lessOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price" lessOrEqualThan "1000",
                allMatch("item.price" greaterOrEqualThan "1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price" greaterOrEqualThan "1000",
                allMatch("item.price" greaterOrEqualThan "1000")
            ),
            false
        ),

        Arguments.of(
            anyMatch(
                "item.price" lessOrEqualThan "1000",
                anyMatch("item.price" lessOrEqualThan "1000"),
                allMatch("item.price" lessOrEqualThan "1000"),
                noneMatch("item.price" greaterOrEqualThan "1000"),
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price" greaterOrEqualThan "1000",
                anyMatch("item.price" lessOrEqualThan "1000"),
                allMatch("item.price" lessOrEqualThan "1000"),
                noneMatch("item.price" greaterOrEqualThan "1000"),
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price" greaterOrEqualThan "1000",
                anyMatch("item.price" greaterOrEqualThan "1000"),
                allMatch("item.price" lessOrEqualThan "1000"),
                noneMatch("item.price" greaterOrEqualThan "1000"),
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price" greaterOrEqualThan "1000",
                anyMatch("item.price" greaterOrEqualThan "1000"),
                allMatch("item.price" greaterOrEqualThan "1000"),
                noneMatch("item.price" greaterOrEqualThan "1000"),
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price" greaterOrEqualThan "1000",
                anyMatch("item.price" greaterOrEqualThan "1000"),
                allMatch("item.price" greaterOrEqualThan "1000"),
                noneMatch("item.price" lessOrEqualThan "1000"),
            ),
            false
        ),
        Arguments.of(
            "item.arrTags" expContains "\"in_array\"",
            true
        ),
        Arguments.of(
            "item.arrTags" expContains "\"not_in_array\"",
            false
        ),
    )
}
