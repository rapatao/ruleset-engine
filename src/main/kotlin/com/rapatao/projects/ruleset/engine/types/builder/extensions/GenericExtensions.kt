package com.rapatao.projects.ruleset.engine.types.builder.extensions

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder

/**
 * Converts the receiver object to an instance of `Expression` if it is not already an `Expression`.
 *
 * @return The `Expression` instance representing the receiver object, or the receiver object itself
 *         if it is already an `Expression`.
 */
fun Any.asExpression(): Expression =
    this.takeIf { it !is Expression }?.let { ExpressionBuilder.isTrue(it) } ?: this as Expression

/**
 * Applies the specified [OnFailure] behavior when evaluating the current [Expression] fails.
 *
 * @param use The behavior to be applied when evaluation fails.
 * @return An [Expression] with the specified [OnFailure] behavior.
 */
infix fun Expression.ifFail(use: OnFailure): Expression = Expression(
    left = this.left, operator = this.operator, right = this.right, onFailure = use,
)
