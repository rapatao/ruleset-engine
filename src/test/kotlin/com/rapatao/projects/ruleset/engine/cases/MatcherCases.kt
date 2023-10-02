package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.allMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.anyMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.noneMatch
import org.junit.jupiter.params.provider.Arguments

object MatcherCases {

    @Suppress("LongMethod")
    fun cases(): List<Arguments> = listOf(
        Arguments.of(
            noneMatch("item.price >= 1000"),
            true
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
                allMatch("item.price >= 1000")
            ),
            true
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                anyMatch("item.price <= 1000"),
                allMatch("item.price >= 1000")
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                anyMatch("item.price >= 1000"),
                allMatch("item.price <= 1000")
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                "item.price >= 1000",
                anyMatch("item.price <= 1000"),
                allMatch("item.price <= 1000")
            ),
            false
        ),
    )
}
