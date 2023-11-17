package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.context.EvalEngine
import com.rapatao.projects.ruleset.engine.evaluator.rhino.RhinoEvalEngine
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure

/**
 * The Evaluator class is used to evaluate a given rule expression against input data.
 *
 * @property engine The factory used to create a context for evaluating the expressions.
 */
class Evaluator(
    private val engine: EvalEngine = RhinoEvalEngine(),
) {

    /**
     * Evaluates the given rule expression against the provided input data.
     *
     * @param expression The expression to be evaluated.
     * @param inputData The input data to be used in the evaluation.
     * @return `true` if the rule expression evaluates to `true`, `false` otherwise.
     */
    fun evaluate(expression: Expression, inputData: Any): Boolean {
        return usingFailureWrapper(expression.onFailure) {
            engine.call(inputData) { context ->
                val processIsTrue = expression.takeIf { v -> v.parseable() }?.processExpression(context) ?: true
                val processNoneMatch = expression.noneMatch?.processNoneMatch(context) ?: true
                val processAnyMatch = expression.anyMatch?.processAnyMatch(context) ?: true
                val processAllMatch = expression.allMatch?.processAllMatch(context) ?: true

                processIsTrue && processNoneMatch && processAnyMatch && processAllMatch
            }
        }
    }

    private fun List<Expression>.processNoneMatch(
        context: EvalContext,
    ): Boolean {
        val noneMatch = this.firstOrNull {
            val isTrue = it.takeIf { v -> v.parseable() }?.processExpression(context) == true
            val noneMatch = it.noneMatch?.processNoneMatch(context) == false
            val anyMatch = it.anyMatch?.processAnyMatch(context) == true
            val allMatch = it.allMatch?.processAllMatch(context) == true

            isTrue || noneMatch || anyMatch || allMatch
        } == null

        return noneMatch
    }

    private fun List<Expression>.processAnyMatch(
        context: EvalContext,
    ): Boolean {
        val anyMatch = this.firstOrNull {
            val isTrue = it.takeIf { v -> v.parseable() }?.processExpression(context) == true
            val anyMatch = it.anyMatch?.processAnyMatch(context) ?: false
            val noneMatch = it.noneMatch?.processNoneMatch(context) == true
            val allMatch = it.allMatch?.processAllMatch(context) == true

            isTrue || anyMatch || noneMatch || allMatch
        } != null

        return anyMatch
    }

    private fun List<Expression>.processAllMatch(
        context: EvalContext,
    ): Boolean {
        val allMatch = this.firstOrNull {
            val isTrue = it.takeIf { v -> v.parseable() }?.processExpression(context) == false
            val anyMatch = it.anyMatch?.processAnyMatch(context) == false
            val noneMatch = it.noneMatch?.processNoneMatch(context) == false
            val allMatch = it.allMatch?.processAllMatch(context) == false

            isTrue || anyMatch || noneMatch || allMatch
        } == null

        return allMatch
    }

    private fun Expression.processExpression(context: EvalContext): Boolean {
        return usingFailureWrapper(this.onFailure) {
            context.process(this)
        }
    }

    private fun usingFailureWrapper(onFailure: OnFailure, block: () -> Boolean): Boolean {
        return try {
            block()
        } catch (@SuppressWarnings("TooGenericExceptionCaught") e: Exception) {
            when (onFailure) {
                OnFailure.TRUE -> true
                OnFailure.FALSE -> false
                OnFailure.THROW -> throw e
            }
        }
    }
}
