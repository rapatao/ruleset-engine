package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.builder.equalsTo
import org.junit.jupiter.params.provider.Arguments

object OnFailureCases {

    fun cases(): List<Arguments> = anyMatchCases() + allMatchCases() + noneMatchCases()

    private fun anyMatchCases(): List<Arguments> = listOf(
        Arguments.of(
            Expression(
                anyMatch = listOf(
                    "item.non.existing.field" equalsTo 10,
                )
            ),
            false,
            true,
        ),
        Arguments.of(
            Expression(
                onFailure = OnFailure.THROW,
                anyMatch = listOf(
                    "item.non.existing.field" equalsTo 10,
                )
            ),
            false,
            true,
        ),
        Arguments.of(
            Expression(
                onFailure = OnFailure.FALSE,
                anyMatch = listOf(
                    "item.non.existing.field" equalsTo 10,
                )
            ),
            false,
            false,
        ),
        Arguments.of(
            Expression(
                onFailure = OnFailure.TRUE,
                anyMatch = listOf(
                    "item.non.existing.field" equalsTo 10,
                )
            ),
            true,
            false,
        ),
    )

    private fun allMatchCases(): List<Arguments> = listOf(
        Arguments.of(
            Expression(
                allMatch = listOf(
                    "item.non.existing.field" equalsTo 10,
                )
            ),
            false,
            true,
        ),
        Arguments.of(
            Expression(
                onFailure = OnFailure.THROW,
                allMatch = listOf(
                    "item.non.existing.field" equalsTo 10,
                )
            ),
            false,
            true,
        ),
        Arguments.of(
            Expression(
                onFailure = OnFailure.FALSE,
                allMatch = listOf(
                    "item.non.existing.field" equalsTo 10,
                )
            ),
            false,
            false,
        ),
        Arguments.of(
            Expression(
                onFailure = OnFailure.TRUE,
                allMatch = listOf(
                    "item.non.existing.field" equalsTo 10,
                )
            ),
            true,
            false,
        ),
    )

    private fun noneMatchCases(): List<Arguments> = listOf(
        Arguments.of(
            Expression(
                noneMatch = listOf(
                    "item.non.existing.field" equalsTo 10,
                )
            ),
            false,
            true,
        ),
        Arguments.of(
            Expression(
                onFailure = OnFailure.THROW,
                noneMatch = listOf(
                    "item.non.existing.field" equalsTo 10,
                )
            ),
            false,
            true,
        ),
        Arguments.of(
            Expression(
                onFailure = OnFailure.FALSE,
                noneMatch = listOf(
                    "item.non.existing.field" equalsTo 10,
                )
            ),
            false,
            false,
        ),
        Arguments.of(
            Expression(
                onFailure = OnFailure.TRUE,
                noneMatch = listOf(
                    "item.non.existing.field" equalsTo 10,
                )
            ),
            true,
            false,
        ),
    )
}
