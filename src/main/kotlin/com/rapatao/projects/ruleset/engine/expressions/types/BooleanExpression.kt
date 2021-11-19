package com.rapatao.projects.ruleset.engine.expressions.types

import com.rapatao.projects.ruleset.engine.expressions.Expression

class BooleanExpression(
    private val field: String,
    private val operator: Operator,
    private val value: Any,
) : Expression {
    override fun parse(): String = "$field ${operator.operator} $value"

    enum class Operator(
        val operator: String
    ) {
        EQUALS("=="),
        GREATER_THAN(">"),
        GREATER_OR_EQUAL_THAN(">="),
        LESS_THAN("<"),
        LESS_OR_EQUAL_THAN("<=")
    }
}
