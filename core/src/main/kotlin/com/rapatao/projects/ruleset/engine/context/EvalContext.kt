package com.rapatao.projects.ruleset.engine.context

import com.rapatao.projects.ruleset.engine.Evaluator
import com.rapatao.projects.ruleset.engine.types.operators.Operator

/**
 * Represents an evaluation context for processing expressions.
 */
interface EvalContext {

    /**
     * Processes the expression using the given operator.
     *
     * @param left The left-hand side of the expression. Can be any type.
     * @param operator The operator to use for processing the expression. This is an instance of [Operator].
     * @param right The right-hand side of the expression. Can be any type.
     *
     * @return `true` if the expression is successfully processed, `false` otherwise.
     */
    fun process(left: Any?, operator: Operator, right: Any?): Boolean

    /**
     * Returns the Evaluator instance used to build this evaluation context.
     *
     * @return The Evaluator implementation that was used to create this EvalContext.
     */
    fun engine(): Evaluator
}
