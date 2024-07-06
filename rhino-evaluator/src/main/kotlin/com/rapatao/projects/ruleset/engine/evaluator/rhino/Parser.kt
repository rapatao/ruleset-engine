package com.rapatao.projects.ruleset.engine.evaluator.rhino

import com.rapatao.projects.ruleset.engine.types.Expression

internal object Parser {
    fun parse(expression: Expression): String {
        return when (expression.operator) {
            "equals" -> "==".formatComparison(expression)
            "not_equals" -> "!=".formatComparison(expression)
            "greater_than" -> ">".formatComparison(expression)
            "greater_or_equal_than" -> ">=".formatComparison(expression)
            "less_than" -> "<".formatComparison(expression)
            "less_or_equal_than" -> "<=".formatComparison(expression)
            "starts_with" -> "startsWith".formatWithOperation(expression)
            "ends_with" -> "endsWith".formatWithOperation(expression)
            "contains" -> formatContainsOperation(expression)
            else -> error("when evaluation an expression, the operator cannot be null")
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
