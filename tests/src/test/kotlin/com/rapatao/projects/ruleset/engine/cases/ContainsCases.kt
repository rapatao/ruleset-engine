package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.types.builder.extensions.expContains
import org.junit.jupiter.params.provider.Arguments

object ContainsCases {
    fun cases(): List<Arguments> = listOf(
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
    )
}
