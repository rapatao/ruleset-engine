package com.rapatao.projects.ruleset.engine.evaluator.rhino

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Operator

internal object Parser {
    fun parse(expression: Expression): String {
        val operator = when (expression.operator) {
            Operator.GREATER_THAN -> ">"
            Operator.GREATER_OR_EQUAL_THAN -> ">="
            Operator.LESS_THAN -> "<"
            Operator.LESS_OR_EQUAL_THAN -> "<="
            Operator.NOT_EQUALS -> "!="
            else -> "=="
        }

        return "(${expression.left}) $operator (${expression.right})"
    }
}
