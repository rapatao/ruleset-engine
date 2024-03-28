package com.rapatao.projects.ruleset.engine.types.builder.extensions

import com.rapatao.projects.ruleset.engine.types.Operator
import com.rapatao.projects.ruleset.engine.types.builder.BetweenBuilder

/**
 * Creates a [BetweenBuilder] object to build a between condition.
 *
 * @param from The lower boundary value.
 * @return A [BetweenBuilder] object to build the between condition.
 */
infix fun Any.from(from: Any): BetweenBuilder = BetweenBuilder(
    left = this, from = from, operator = Operator.GREATER_THAN,
)

/**
 * Creates a [BetweenBuilder] with the given left operand, starting value, and comparison operator
 *
 * @param from The starting value of the range, inclusive
 * @return The [BetweenBuilder] instance for further chaining
 */
infix fun Any.fromInclusive(from: Any): BetweenBuilder = BetweenBuilder(
    left = this, from = from, operator = Operator.GREATER_OR_EQUAL_THAN,
)
