package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Operator

internal object Parser {
    fun parse(expression: Expression, left: Any?, right: Any?) =
        when (expression.operator) {
            Operator.EQUALS -> left == right
            Operator.NOT_EQUALS -> left != right
            Operator.GREATER_THAN -> left.comparable() > right
            Operator.GREATER_OR_EQUAL_THAN -> left.comparable() >= right
            Operator.LESS_THAN -> left.comparable() < right
            Operator.LESS_OR_EQUAL_THAN -> left.comparable() <= right
            Operator.STARTS_WITH -> left.toString().startsWith(right.toString())
            Operator.ENDS_WITH -> left.toString().endsWith(right.toString())
            Operator.CONTAINS -> left.checkContains(right)
            null -> error("when evaluation an expression, the operator cannot be null")
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
