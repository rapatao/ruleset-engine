package com.rapatao.projects.ruleset.engine.context

import com.rapatao.projects.ruleset.engine.types.Expression

/**
 * Represents an evaluation context for processing expressions.
 */
fun interface EvalContext {

    /**
     * Processes an expression.
     *
     * @param expression the expression to process
     * @return true if the expression is successfully processed, false otherwise
     * @throws Exception if the expression processing fails and onFailure is set to THROW
     */
    fun process(expression: Expression): Boolean
}
