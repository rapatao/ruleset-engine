package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import com.rapatao.projects.ruleset.engine.types.Expression

internal object Parser {
    fun parse(expression: Expression, left: Any?, right: Any?) =
        when (expression.operator) {
            "equals" -> left == right
            "not_equals" -> left != right
            "greater_than" -> left.comparable() > right
            "greater_or_equal_than" -> left.comparable() >= right
            "less_than" -> left.comparable() < right
            "less_or_equal_than" -> left.comparable() <= right
            "starts_with" -> left.toString().startsWith(right.toString())
            "ends_with" -> left.toString().endsWith(right.toString())
            "contains" -> left.checkContains(right)
            else -> error("when evaluation an expression, the operator cannot be null")
        }

    @Suppress("UNCHECKED_CAST")
    private fun <T> T.comparable() = this as Comparable<T>

    private fun Any?.checkContains(value: Any?): Boolean {
        return when {
            this is String && value is String -> this.contains(value)
            this is Collection<*> -> this.contains(value)
            this is Array<*> -> this.contains(value)
            else -> throw UnsupportedOperationException("contains doesn't support ${this?.javaClass} type")
        }
    }
}
