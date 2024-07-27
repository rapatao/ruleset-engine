package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure
import org.junit.jupiter.params.provider.Arguments

object InvalidOperatorCases {
    fun cases(): List<Arguments> = listOf(
        // missing operator
        Arguments.of(
            Expression(left = true, right = true, onFailure = OnFailure.FALSE),
            false,
        ),
        Arguments.of(
            Expression(left = true, right = true, onFailure = OnFailure.TRUE),
            true,
        ),
        Arguments.of(
            Expression(left = true, right = true, onFailure = OnFailure.THROW),
            false,
        ),
        // not declared operator
        Arguments.of(
            Expression(left = true, right = true, operator = "_non-existing_", onFailure = OnFailure.FALSE),
            false,
        ),
        Arguments.of(
            Expression(left = true, right = true, operator = "_non-existing_", onFailure = OnFailure.TRUE),
            true,
        ),
        Arguments.of(
            Expression(left = true, right = true, operator = "_non-existing_", onFailure = OnFailure.THROW),
            false,
        ),
    )
}
