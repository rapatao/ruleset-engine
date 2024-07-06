package com.rapatao.projects.ruleset.engine.types.builder.extensions

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder

/**
 * Creates an expression to check if the current string contains the specified substring.
 *
 * @receiver The string in which to check for the presence of the substring.
 * @param right The substring to search for within the current string.
 * @return An [Expression] object representing the containment check, with the operator set to "contains".
 */
infix fun String.expContains(right: String): Expression = ExpressionBuilder.left(this).contains(right)

/**
 * Creates an expression to check if the current list contains the specified element.
 *
 * @receiver The list in which to check for the presence of the element.
 * @param right The element to search for within the current list.
 * @return An [Expression] object representing the containment check, with the operator set to "contains".
 */
infix fun List<Any>.expContains(right: Any): Expression = ExpressionBuilder.left(this).contains(right)
