package com.rapatao.projects.ruleset.engine.types

data class Expression(
    val allMatch: List<Expression>? = null,
    val anyMatch: List<Expression>? = null,
    val noneMatch: List<Expression>? = null,
    val left: Any? = null,
    val operator: Operator? = null,
    val right: Any? = null,
    val onFailure: OnFailure = OnFailure.THROW,
) {
    fun operator() = operator
    fun onFailure() = onFailure

    fun parse(): String {
        return "($left) ${operator?.operator} ($right)"
    }

    fun parseable(): Boolean = operator != null

    enum class Operator(val operator: String) {
        EQUALS("=="),
        GREATER_THAN(">"),
        GREATER_OR_EQUAL_THAN(">="),
        LESS_THAN("<"),
        LESS_OR_EQUAL_THAN("<=")
    }
}
