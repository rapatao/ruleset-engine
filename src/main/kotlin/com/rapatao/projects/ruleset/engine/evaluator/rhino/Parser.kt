package com.rapatao.projects.ruleset.engine.evaluator.rhino

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Operator

internal object Parser {
    fun parse(expression: Expression): String {
        return when (expression.operator) {
            Operator.GREATER_THAN -> ">".formatComparison(expression)
            Operator.GREATER_OR_EQUAL_THAN -> ">=".formatComparison(expression)
            Operator.LESS_THAN -> "<".formatComparison(expression)
            Operator.LESS_OR_EQUAL_THAN -> "<=".formatComparison(expression)
            Operator.NOT_EQUALS -> "!=".formatComparison(expression)
            Operator.STARTS_WITH -> "startsWith".formatWithOperation(expression)
            Operator.ENDS_WITH -> "endsWith".formatWithOperation(expression)
            Operator.CONTAINS -> formatContainsOperation(expression)
            else -> "==".formatComparison(expression)
        }
    }

    private fun String.formatComparison(expression: Expression) =
        "(${expression.left}) $this (${expression.right})"

    private fun String.formatWithOperation(expression: Expression) =
        "${expression.left}.${this}(${expression.right})"

    private fun formatContainsOperation(expression: Expression) =
        """
        (function() {
            if (Array.isArray(${expression.left})) {
                return ${expression.left}.includes(${expression.right})
            } else {
                return ${expression.left}.indexOf(${expression.right}) !== -1
            }
        })()
        """.trimIndent()
}
