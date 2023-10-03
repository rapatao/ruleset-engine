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

    fun parseable(): Boolean = operator != null
}
