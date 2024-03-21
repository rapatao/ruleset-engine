package com.rapatao.projects.ruleset.engine.types.builder

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.builder.extensions.asExpression

/**
 * The `MatcherBuilder` class provides static methods for building expressions using matchers.
 */
object MatcherBuilder {

    /**
     * Creates an [Expression] that matches all provided matchers.
     *
     * @param matchers The matchers to be combined into a single expression.
     * @return The created expression that matches all provided matchers.
     */
    fun allMatch(vararg matchers: Any): Expression = Expression(
        allMatch = matchers.map { it.asExpression() }
    )

    /**
     * Creates an expression for verifying if any of the given matchers matches.
     *
     * @param matchers the matchers to be evaluated
     * @return the expression representing the anyMatch operation
     */
    fun anyMatch(vararg matchers: Any) = Expression(
        anyMatch = matchers.map { it.asExpression() }
    )

    /**
     * Creates an instance of [Expression] with [noneMatch] set to the provided [matchers].
     *
     * @param matchers The matchers to check against.
     * @return An instance of [Expression] with [noneMatch] set.
     */
    fun noneMatch(vararg matchers: Any) = Expression(
        noneMatch = matchers.map { it.asExpression() },
    )
}
