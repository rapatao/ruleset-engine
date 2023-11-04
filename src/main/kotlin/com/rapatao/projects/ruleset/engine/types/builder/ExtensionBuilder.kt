package com.rapatao.projects.ruleset.engine.types.builder

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.isFalse
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.isTrue
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.left

/**
 * Converts the receiver object to an instance of `Expression` if it is not already an `Expression`.
 *
 * @return The `Expression` instance representing the receiver object, or the receiver object itself
 *         if it is already an `Expression`.
 */
fun Any.asExpression(): Expression = this.takeIf { it !is Expression }?.let { isTrue(it) } ?: this as Expression

/**
 * Checks if the current value is considered to be "true".
 *
 * @return An [Expression] representing the current value.
 */
fun Any.isTrue(): Expression = isTrue(this)

/**
 * Checks if the given expression evaluates to false.
 *
 * @return True if the expression is false, false otherwise.
 */
fun Any.isFalse(): Expression = isFalse(this)

/**
 * Checks if the current object is equal to the given object using a custom equality expression.
 *
 * @param right The object to compare with.
 * @return An expression representing the equality comparison between the current object and the given object.
 */
infix fun Any.equalsTo(right: Any): Expression = left(this).equalsTo(right)

/**
 * Compares this object with the specified [right] object for inequality.
 *
 * @param right The object to compare with this object.
 * @return An [Expression] representing the inequality comparison result.
 */
infix fun Any.notEqualsTo(right: Any): Expression = left(this).notEqualsTo(right)

/**
 * Returns an expression representing the greater-than comparison between this value and the given value.
 *
 * @param from The value to compare against.
 * @return An [Expression] representing the greater-than comparison.
 */
infix fun Any.greaterThan(from: Any): Expression = left(this).greaterThan(from)

/**
 * Compares two objects and returns an expression representing the "greater than or equal to" comparison.
 *
 * @param from The object to compare against.
 * @return An expression representing the "greater than or equal to" comparison.
 */
infix fun Any.greaterOrEqualThan(from: Any): Expression = left(this).greaterOrEqualThan(from)

/**
 * Compares this object with the specified object and returns an expression representing the result
 * of the less than comparison.
 *
 * @param from the object to compare with
 * @return an expression representing the result of the less than comparison
 */
infix fun Any.lessThan(from: Any): Expression = left(this).lessThan(from)

/**
 * Checks if the current value is less than or equal to the provided value.
 *
 * @param from The value to compare against.
 * @return An [Expression] representing the comparison expression.
 */
infix fun Any.lessOrEqualThan(from: Any): Expression = left(this).lessOrEqualThan(from)

/**
 * Applies the specified [OnFailure] behavior when evaluating the current [Expression] fails.
 *
 * @param use The behavior to be applied when evaluation fails.
 * @return An [Expression] with the specified [OnFailure] behavior.
 */
infix fun Expression.ifFail(use: OnFailure): Expression = Expression(
    left = this.left, operator = this.operator, right = this.right, onFailure = use,
)
