package com.rapatao.projects.ruleset.engine.types.builder.extensions

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder

/**
 * Compares this object with the specified object and returns an expression representing the result
 * of the less than comparison.
 *
 * @param from the object to compare with
 * @return an expression representing the result of the less than comparison
 */
infix fun Any.lessThan(from: Any): Expression = ExpressionBuilder.left(this).lessThan(from)

/**
 * Checks if the current value is less than or equal to the provided value.
 *
 * @param from The value to compare against.
 * @return An [Expression] representing the comparison expression.
 */
infix fun Any.lessOrEqualThan(from: Any): Expression = ExpressionBuilder.left(this).lessOrEqualThan(from)
