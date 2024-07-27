package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.types.builder.extensions.endsWith
import com.rapatao.projects.ruleset.engine.types.builder.extensions.notEndsWith
import com.rapatao.projects.ruleset.engine.types.builder.extensions.notStartsWith
import com.rapatao.projects.ruleset.engine.types.builder.extensions.startsWith
import org.junit.jupiter.params.provider.Arguments

object OperatorWithCases {

    fun cases(): List<Arguments> = listOf(
        Arguments.of(
            "item.name" startsWith "\"product\"",
            true
        ),
        Arguments.of(
            "item.name" startsWith "\"name\"",
            false
        ),
        Arguments.of(
            "item.name" notStartsWith "\"product\"",
            false
        ),
        Arguments.of(
            "item.name" notStartsWith "\"name\"",
            true
        ),
        Arguments.of(
            "item.name" endsWith "\"name\"",
            true
        ),
        Arguments.of(
            "item.name" endsWith "\"product\"",
            false
        ),
        Arguments.of(
            "item.name" notEndsWith "\"name\"",
            false
        ),
        Arguments.of(
            "item.name" notEndsWith "\"product\"",
            true
        ),
    )
}
