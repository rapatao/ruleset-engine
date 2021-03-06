package com.rapatao.projects.ruleset.engine.types

data class BooleanExpression(
    val left: String,
    val operator: Operator,
    val right: Any,
) : Expression {
    override fun parse(): String = "$left ${operator.operator} $right"

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
