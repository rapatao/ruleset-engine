package com.rapatao.projects.ruleset.engine.types.builder.extensions

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder

/**
 * Creates an [Expression] object that represents a "starts with" operation.
 *
 * @param right The value to compare with the left string to check if it starts with it.
 * @return An [Expression] object with the left string, the "starts_with" operator, and the right value.
 */
infix fun String.startsWith(right: Any): Expression = ExpressionBuilder.left(this).startsWith(right)

/**
 * Creates an [Expression] object that represents a "ends with" operation.
 *
 * @param right The value to compare with the left string to check if it ends with it.
 * @return An [Expression] object with the left string, the "ends_with" operator, and the right value.
 */
infix fun String.endsWith(right: Any): Expression = ExpressionBuilder.left(this).endsWith(right)
