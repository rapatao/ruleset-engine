package com.rapatao.projects.ruleset.engine.types.builder

import com.rapatao.projects.ruleset.engine.types.Expression

object MatcherBuilder {

    fun allMatch(vararg matchers: Any): Expression = Expression(
        allMatch = matchers.map { it.asExpression() }
    )

    fun anyMatch(vararg matchers: Any) = Expression(
        anyMatch = matchers.map { it.asExpression() }
    )

    fun noneMatch(vararg matchers: Any) = Expression(
        noneMatch = matchers.map { it.asExpression() },
    )
}
