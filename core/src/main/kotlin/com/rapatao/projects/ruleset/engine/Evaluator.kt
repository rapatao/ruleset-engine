package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure

/**
 * The Evaluator is a base class used to evaluate a given rule expression against input data.
 */
abstract class Evaluator {

    /**
     * Evaluates the given rule expression against the provided input data.
     *
     * @param expression The expression to be evaluated.
     * @param inputData The input data to be used in the evaluation.
     * @return `true` if the rule expression evaluates to `true`, `false` otherwise.
     */
    fun evaluate(expression: Expression, inputData: Any): Boolean {
        return usingFailureWrapper(expression.onFailure) {
            call(inputData) {
                val processIsTrue = expression.takeIf { v -> v.parseable() }?.processExpression(it) ?: true
                val processNoneMatch = expression.noneMatch?.processNoneMatch(it) ?: true
                val processAnyMatch = expression.anyMatch?.processAnyMatch(it) ?: true
                val processAllMatch = expression.allMatch?.processAllMatch(it) ?: true

                processIsTrue && processNoneMatch && processAnyMatch && processAllMatch
            }
        }
    }

    /**
     * Executes the provided block of code with the given input data and returns a boolean value indicating
     * the success or failure of the execution.
     *
     * @param inputData The input data to be used in the execution.
     * @param block A lambda function that takes in a context and a scope as parameters and returns a boolean value.
     *              The context represents the context in which the execution takes place, and the scope represents
     *              the scope of the execution.
     * @return The result of the execution.
     */
    abstract fun <T> call(inputData: Any, block: (context: EvalContext) -> T): T

    /**
     * Returns the name of the evaluation engine. This can be used to identify the engine in logging or debugging.
     *
     * @return The name of the evaluation engine.
     */
    abstract fun name(): String

    private fun List<Expression>.processNoneMatch(context: EvalContext): Boolean {
        return this.none {
            val isTrue = it.takeIf { v -> v.parseable() }?.processExpression(context) == true
            val noneMatch = it.noneMatch?.processNoneMatch(context) == false
            val anyMatch = it.anyMatch?.processAnyMatch(context) == true
            val allMatch = it.allMatch?.processAllMatch(context) == true

            isTrue || noneMatch || anyMatch || allMatch
        }
    }

    private fun List<Expression>.processAnyMatch(context: EvalContext): Boolean {
        return this.any {
            val isTrue = it.takeIf { v -> v.parseable() }?.processExpression(context) == true
            val anyMatch = it.anyMatch?.processAnyMatch(context) ?: false
            val noneMatch = it.noneMatch?.processNoneMatch(context) == true
            val allMatch = it.allMatch?.processAllMatch(context) == true

            isTrue || anyMatch || noneMatch || allMatch
        }
    }

    private fun List<Expression>.processAllMatch(context: EvalContext): Boolean {
        return this.none {
            val isTrue = it.takeIf { v -> v.parseable() }?.processExpression(context) == false
            val anyMatch = it.anyMatch?.processAnyMatch(context) == false
            val noneMatch = it.noneMatch?.processNoneMatch(context) == false
            val allMatch = it.allMatch?.processAllMatch(context) == false

            isTrue || anyMatch || noneMatch || allMatch
        }
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
