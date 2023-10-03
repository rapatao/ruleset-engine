package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.allMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.anyMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.noneMatch
import org.junit.jupiter.params.provider.Arguments

object MatcherCases {

    fun cases(): List<Arguments> = noneMatchCases() + allMatchCases() + anyMatchCases()

    @Suppress("LongMethod")
    private fun noneMatchCases(): List<Arguments> = listOf(
        Arguments.of(
            noneMatch("item.price >= 1000"),
            true
        ),
        Arguments.of(
            noneMatch("item.price <= 1000"),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                "item.price >= 1000",
            ),
            true
        ),
        Arguments.of(
            noneMatch(
                "item.price <= 1000",
                "item.price >= 1000",
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                "item.price <= 1000",
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price <= 1000",
                "item.price <= 1000",
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                anyMatch("item.price >= 1000")
            ),
            true
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                anyMatch("item.price <= 1000")
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                noneMatch("item.price >= 1000")
            ),
            true
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                noneMatch("item.price <= 1000")
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                allMatch("item.price >= 1000")
            ),
            true
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                allMatch("item.price <= 1000")
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                anyMatch("item.price >= 1000"),
                allMatch("item.price >= 1000"),
                noneMatch("item.price >= 1000"),
            ),
            true
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                anyMatch("item.price <= 1000"),
                allMatch("item.price >= 1000"),
                noneMatch("item.price >= 1000")
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                anyMatch("item.price >= 1000"),
                allMatch("item.price <= 1000"),
                noneMatch("item.price >= 1000")
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                anyMatch("item.price >= 1000"),
                allMatch("item.price >= 1000"),
                noneMatch("item.price <= 1000")
            ),
            false
        ),
    )

    @Suppress("LongMethod")
    private fun allMatchCases(): List<Arguments> = listOf(
        Arguments.of(
            allMatch("item.price <= 1000"),
            true
        ),
        Arguments.of(
            allMatch("item.price >= 1000"),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price <= 1000",
                "item.price <= 1000",
            ),
            true
        ),
        Arguments.of(
            allMatch(
                "item.price >= 1000",
                "item.price <= 1000",
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price <= 1000",
                "item.price >= 1000",
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price >= 1000",
                "item.price >= 1000",
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price <= 1000",
                anyMatch("item.price <= 1000")
            ),
            true
        ),
        Arguments.of(
            allMatch(
                "item.price <= 1000",
                anyMatch("item.price >= 1000")
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price <= 1000",
                noneMatch("item.price >= 1000")
            ),
            true
        ),
        Arguments.of(
            allMatch(
                "item.price <= 1000",
                noneMatch("item.price <= 1000")
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price <= 1000",
                allMatch("item.price <= 1000")
            ),
            true
        ),
        Arguments.of(
            allMatch(
                "item.price <= 1000",
                allMatch("item.price >= 1000")
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price <= 1000",
                anyMatch("item.price <= 1000"),
                allMatch("item.price <= 1000"),
                noneMatch("item.price >= 1000"),
            ),
            true
        ),
        Arguments.of(
            allMatch(
                "item.price <= 1000",
                anyMatch("item.price >= 1000"),
                allMatch("item.price <= 1000"),
                noneMatch("item.price >= 1000")
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price <= 1000",
                anyMatch("item.price <= 1000"),
                allMatch("item.price >= 1000"),
                noneMatch("item.price >= 1000")
            ),
            false
        ),
        Arguments.of(
            allMatch(
                "item.price <= 1000",
                anyMatch("item.price <= 1000"),
                allMatch("item.price <= 1000"),
                noneMatch("item.price <= 1000")
            ),
            false
        ),
    )

    @Suppress("LongMethod")
    private fun anyMatchCases(): List<Arguments> = listOf(
        Arguments.of(
            anyMatch("item.price <= 1000"),
            true
        ),
        Arguments.of(
            anyMatch("item.price >= 1000"),
            false
        ),
        Arguments.of(
            anyMatch(
                "item.price <= 1000",
                "item.price >= 1000"
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price >= 1000",
                "item.price >= 1000"
            ),
            false
        ),
        Arguments.of(
            anyMatch(
                "item.price <= 1000",
                anyMatch("item.price <= 1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price >= 1000",
                anyMatch("item.price <= 1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price <= 1000",
                anyMatch("item.price >= 1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price >= 1000",
                anyMatch("item.price >= 1000")
            ),
            false
        ),
        Arguments.of(
            anyMatch(
                "item.price <= 1000",
                noneMatch("item.price >= 1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price >= 1000",
                noneMatch("item.price >= 1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price <= 1000",
                noneMatch("item.price <= 1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price >= 1000",
                noneMatch("item.price <= 1000")
            ),
            false
        ),
        Arguments.of(
            anyMatch(
                "item.price <= 1000",
                allMatch("item.price <= 1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price >= 1000",
                allMatch("item.price <= 1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price <= 1000",
                allMatch("item.price >= 1000")
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price >= 1000",
                allMatch("item.price >= 1000")
            ),
            false
        ),

        Arguments.of(
            anyMatch(
                "item.price <= 1000",
                anyMatch("item.price <= 1000"),
                allMatch("item.price <= 1000"),
                noneMatch("item.price >= 1000"),
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price >= 1000",
                anyMatch("item.price <= 1000"),
                allMatch("item.price <= 1000"),
                noneMatch("item.price >= 1000"),
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price >= 1000",
                anyMatch("item.price >= 1000"),
                allMatch("item.price <= 1000"),
                noneMatch("item.price >= 1000"),
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price >= 1000",
                anyMatch("item.price >= 1000"),
                allMatch("item.price >= 1000"),
                noneMatch("item.price >= 1000"),
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                "item.price >= 1000",
                anyMatch("item.price >= 1000"),
                allMatch("item.price >= 1000"),
                noneMatch("item.price <= 1000"),
            ),
            false
        ),
    )
}
