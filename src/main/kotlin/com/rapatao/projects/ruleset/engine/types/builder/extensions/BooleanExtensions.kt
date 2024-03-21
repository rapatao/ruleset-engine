package com.rapatao.projects.ruleset.engine.types.builder.extensions

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder

/**
 * Checks if the current value is considered to be "true".
 *
 * @return An [Expression] representing the current value.
 */
fun Any.isTrue(): Expression = ExpressionBuilder.isTrue(this)

/**
 * Checks if the given expression evaluates to false.
 *
 * @return True if the expression is false, false otherwise.
 */
fun Any.isFalse(): Expression = ExpressionBuilder.isFalse(this)

/**
 * Checks if the current object is equal to the given object using a custom equality expression.
 *
 * @param right The object to compare with.
 * @return An expression representing the equality comparison between the current object and the given object.
 */
infix fun Any.equalsTo(right: Any): Expression = ExpressionBuilder.left(this).equalsTo(right)

/**
 * Compares this object with the specified [right] object for inequality.
 *
 * @param right The object to compare with this object.
 * @return An [Expression] representing the inequality comparison result.
 */
infix fun Any.notEqualsTo(right: Any): Expression = ExpressionBuilder.left(this).notEqualsTo(right)

