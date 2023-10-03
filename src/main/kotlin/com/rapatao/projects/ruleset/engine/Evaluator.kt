package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.context.ContextFactory
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.Operator
import org.mozilla.javascript.Context
import org.mozilla.javascript.Script
import org.mozilla.javascript.ScriptableObject

class Evaluator(
    val contextFactory: ContextFactory = ContextFactory(),
) {

    fun evaluate(rule: Expression, inputData: Any): Boolean {
        return contextFactory.call(inputData) { context, scope ->
            val processIsTrue = rule.takeIf { v -> v.parseable() }?.processExpression(context, scope) ?: true
            val processNoneMatch = rule.noneMatch?.processNoneMatch(context, scope) ?: true
            val processAnyMatch = rule.anyMatch?.processAnyMatch(context, scope) ?: true
            val processAllMatch = rule.allMatch?.processAllMatch(context, scope) ?: true

            processIsTrue && processNoneMatch && processAnyMatch && processAllMatch
        }
    }

    private fun List<Expression>.processNoneMatch(
        context: Context,
        scope: ScriptableObject,
    ): Boolean {
        val noneMatch = this.firstOrNull {
            val isTrue = it.takeIf { v -> v.parseable() }?.processExpression(context, scope) == true
            val noneMatch = it.noneMatch?.processNoneMatch(context, scope) == false
            val anyMatch = it.anyMatch?.processAnyMatch(context, scope) == true
            val allMatch = it.allMatch?.processAllMatch(context, scope) == true

            isTrue || noneMatch || anyMatch || allMatch
        } == null

        return noneMatch
    }

    private fun List<Expression>.processAnyMatch(
        context: Context,
        scope: ScriptableObject,
    ): Boolean {
        val anyMatch = this.firstOrNull {
            val isTrue = it.takeIf { v -> v.parseable() }?.processExpression(context, scope) == true
            val anyMatch = it.anyMatch?.processAnyMatch(context, scope) ?: false
            val noneMatch = it.noneMatch?.processNoneMatch(context, scope) == true
            val allMatch = it.allMatch?.processAllMatch(context, scope) == true

            isTrue || anyMatch || noneMatch || allMatch
        } != null

        return anyMatch
    }

    private fun List<Expression>.processAllMatch(
        context: Context,
        scope: ScriptableObject,
    ): Boolean {
        val allMatch = this.firstOrNull {
            val isTrue = it.takeIf { v -> v.parseable() }?.processExpression(context, scope) == false
            val anyMatch = it.anyMatch?.processAnyMatch(context, scope) == false
            val noneMatch = it.noneMatch?.processNoneMatch(context, scope) == false
            val allMatch = it.allMatch?.processAllMatch(context, scope) == false

            isTrue || anyMatch || noneMatch || allMatch
        } == null

        return allMatch
    }

    private fun Expression.processExpression(
        context: Context,
        scope: ScriptableObject,
    ): Boolean {
        return try {
            true == this.asScript(context)
                .exec(context, scope)
        } catch (@SuppressWarnings("TooGenericExceptionCaught") e: Exception) {
            when (this.onFailure) {
                OnFailure.TRUE -> true
                OnFailure.FALSE -> false
                OnFailure.THROW -> throw e
            }
        }
    }

    private fun Expression.asScript(context: Context): Script {
        val operator = when (this.operator) {
            Operator.GREATER_THAN -> ">"
            Operator.GREATER_OR_EQUAL_THAN -> ">="
            Operator.LESS_THAN -> "<"
            Operator.LESS_OR_EQUAL_THAN -> "<="
            else -> "=="
        }

        val script = "(${this.left}) $operator (${this.right})"

        return context.compileString(
            "true == ($script)",
            script,
            0,
            null,
        )
    }
}
