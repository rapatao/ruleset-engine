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

    fun isValid(): Boolean {
        val any = anyMatch?.map { it.isValid() }?.firstOrNull { !it } ?: true
        val none = noneMatch?.map { it.isValid() }?.firstOrNull { !it } ?: true
        val all = allMatch?.map { it.isValid() }?.firstOrNull { !it } ?: true

        val has = anyMatch == null && noneMatch == null && allMatch == null && parseable()
        val group = anyMatch != null || noneMatch != null || allMatch != null
        val something = (any && none && all) || parseable()

        return (has || group) && something
    }
}
