package com.rapatao.projects.ruleset.engine.context

import com.rapatao.projects.ruleset.engine.types.operators.Operator

/**
 * Represents an evaluation context for processing expressions.
 */
fun interface EvalContext {

    /**
     * Process the expression using the given operator
     *
     * @return true if the expression is successfully processed, false otherwise
     */
    fun process(left: Any?, operator: Operator, right: Any?): Boolean
}
