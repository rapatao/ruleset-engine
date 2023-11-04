package com.rapatao.projects.ruleset.engine.types

/**
 * Represents an expression used in a logical query.
 *
 * An expression can consist of one or more sub-expressions and an operator.
 * It can also specify the action to take if the expression fails.
 *
 * @property allMatch A list of sub-expressions that all must match.
 * @property anyMatch A list of sub-expressions where at least one must match.
 * @property noneMatch A list of sub-expressions where none must match.
 * @property left The left operand of the expression.
 * @property operator The operator to apply to the expression.
 * @property right The right operand of the expression.
 * @property onFailure The action to take if the expression fails.
 */
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

    /**
     * Checks if the current object is valid.
     *
     * @return Boolean value indicating whether the object is valid.
     */
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
