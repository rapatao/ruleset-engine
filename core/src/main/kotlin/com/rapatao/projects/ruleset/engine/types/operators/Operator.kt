package com.rapatao.projects.ruleset.engine.types.operators

import com.rapatao.projects.ruleset.engine.context.EvalContext

/**
 * This interface defines how the Engines must validate the expression for a given Operator
 */
interface Operator {

    /**
     * Compare the given operands
     *
     * @return true when equal, otherwise false
     */
    fun process(context: EvalContext, left: Any?, right: Any?): Boolean

    /**
     * Returns the operator representation name, such as "equals", "not_equals", etc.
     *
     * @return The operator name
     */
    fun name(): String
}
