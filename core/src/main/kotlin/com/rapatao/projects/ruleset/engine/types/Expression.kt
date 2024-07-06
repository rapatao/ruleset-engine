package com.rapatao.projects.ruleset.engine.types

import com.rapatao.projects.ruleset.engine.Evaluator

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
    val operator: String? = null,
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
    fun isValid(engine: Evaluator): Boolean {
        val any = anyMatch?.map { it.isValid(engine) }?.firstOrNull { !it } ?: true
        val none = noneMatch?.map { it.isValid(engine) }?.firstOrNull { !it } ?: true
        val all = allMatch?.map { it.isValid(engine) }?.firstOrNull { !it } ?: true

        val something = (any && none && all) || parseable()

        return isValidGroup() && something && isValidOperator(engine)
    }

    private fun isValidOperator(engine: Evaluator): Boolean {
        val validOperator = operator == null || engine.operator(operator) != null
        return validOperator
    }

    private fun isValidGroup(): Boolean {
        val has = anyMatch == null && noneMatch == null && allMatch == null && parseable()
        val group = anyMatch != null || noneMatch != null || allMatch != null

        return (has || group)
    }
}
