package com.rapatao.projects.ruleset.engine.types.builder.extensions

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder

/**
 * Returns an expression representing the greater-than comparison between this value and the given value.
 *
 * @param from The value to compare against.
 * @return An [Expression] representing the greater-than comparison.
 */
infix fun Any.greaterThan(from: Any): Expression = ExpressionBuilder.left(this).greaterThan(from)

/**
 * Compares two objects and returns an expression representing the "greater than or equal to" comparison.
 *
 * @param from The object to compare against.
 * @return An expression representing the "greater than or equal to" comparison.
 */
infix fun Any.greaterOrEqualThan(from: Any): Expression = ExpressionBuilder.left(this).greaterOrEqualThan(from)
