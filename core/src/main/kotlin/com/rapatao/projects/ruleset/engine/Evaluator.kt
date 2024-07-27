package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.operators.Operator

/**
 * The Evaluator is a base class used to evaluate a given rule expression against input data.
 */
abstract class Evaluator(
    operators: List<Operator>,
) {

    private val declaredOperators = operators.associateBy { it.name().lowercase() }

    /**
     * Evaluates the given rule expression against the provided input data.
     *
     * @param expression The expression to be evaluated.
     * @param inputData The input data to be used in the evaluation.
     * @return `true` if the rule expression evaluates to `true`, `false` otherwise.
     */
    fun evaluate(expression: Expression, inputData: Any): Boolean {
        return usingFailureWrapper(expression.onFailure) {
            require(expression.isValid(this)) { "expression $expression is not valid" }

            call(inputData) {
                expression.takeIf { v -> v.parseable() }?.processExpression(it) ?: true &&
                    expression.noneMatch?.processNoneMatch(it) ?: true &&
                    expression.anyMatch?.processAnyMatch(it) ?: true &&
                    expression.allMatch?.processAllMatch(it) ?: true
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

    /**
     * Return the operator implementation for the given name.
     *
     * @return The operator.
     */
    fun operator(name: String): Operator? = declaredOperators[name]

    private fun List<Expression>.processNoneMatch(context: EvalContext): Boolean {
        return this.none {
            usingFailureWrapper(it.onFailure) {
                it.takeIf { v -> v.parseable() }?.processExpression(context) == true ||
                    it.noneMatch?.processNoneMatch(context) == false ||
                    it.anyMatch?.processAnyMatch(context) == true ||
                    it.allMatch?.processAllMatch(context) == true
            }
        }
    }

    private fun List<Expression>.processAnyMatch(context: EvalContext): Boolean {
        return this.any {
            usingFailureWrapper(it.onFailure) {
                it.takeIf { v -> v.parseable() }?.processExpression(context) == true ||
                    it.anyMatch?.processAnyMatch(context) ?: false ||
                    it.noneMatch?.processNoneMatch(context) == true ||
                    it.allMatch?.processAllMatch(context) == true
            }
        }
    }

    private fun List<Expression>.processAllMatch(context: EvalContext): Boolean {
        return this.none {
            usingFailureWrapper(it.onFailure) {
                it.takeIf { v -> v.parseable() }?.processExpression(context) == false ||
                    it.anyMatch?.processAnyMatch(context) == false ||
                    it.noneMatch?.processNoneMatch(context) == false ||
                    it.allMatch?.processAllMatch(context) == false
            }
        }
    }

    private fun Expression.processExpression(context: EvalContext): Boolean {
        return usingFailureWrapper(this.onFailure) {
            requireNotNull(this.operator) { "expression operator must not be null" }

            val operator = operator(this.operator)
            requireNotNull(operator) { "Unknown operator: $operator" }

            context.process(left = this.left, operator = operator, right = this.right)
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
