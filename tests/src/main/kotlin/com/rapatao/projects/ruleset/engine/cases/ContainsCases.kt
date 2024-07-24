package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.types.builder.extensions.expContains
import com.rapatao.projects.ruleset.engine.types.builder.extensions.expNotContains
import org.junit.jupiter.params.provider.Arguments

object ContainsCases {
    fun cases(): List<Arguments> = listOf(
        // contains
        Arguments.of(
            "item.name" expContains "\"duct\"",
            true,
        ),
        Arguments.of(
            "item.name" expContains "\"different value\"",
            false,
        ),
        Arguments.of(
            "item.tags" expContains "\"test\"",
            true,
        ),
        Arguments.of(
            "item.tags" expContains "\"different value\"",
            false,
        ),
        // not contains
        Arguments.of(
            "item.name" expNotContains "\"duct\"",
            false,
        ),
        Arguments.of(
            "item.name" expNotContains "\"different value\"",
            true,
        ),
        Arguments.of(
            "item.tags" expNotContains "\"test\"",
            false,
        ),
        Arguments.of(
            "item.tags" expNotContains "\"different value\"",
            true,
        ),
    )
}
