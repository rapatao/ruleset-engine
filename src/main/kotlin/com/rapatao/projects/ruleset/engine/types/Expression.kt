package com.rapatao.projects.ruleset.engine.types

data class Expression(
    val left: Any,
    val operator: Operator,
    val right: Any,
    val onFailure: OnFailure = OnFailure.THROW
) {
    fun parse(): String {
        return "$left ${operator.operator} $right"
    }

    fun onFailure(): OnFailure = onFailure

    fun operator(): Operator = operator

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
