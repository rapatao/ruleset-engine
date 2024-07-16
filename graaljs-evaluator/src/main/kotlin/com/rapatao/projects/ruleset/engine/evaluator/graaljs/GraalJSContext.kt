package com.rapatao.projects.ruleset.engine.evaluator.graaljs

import com.rapatao.projects.ruleset.engine.Evaluator
import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.Operator
import org.graalvm.polyglot.Context

/**
 * GraalJSContext is a class that implements the EvalContext interface.
 * It provides the ability to process expressions using the Graal JS engine.
 *
 * @param evaluator the evaluator implementation instance
 * @param context the GraalJS context object.
 */
class GraalJSContext(
    private val evaluator: Evaluator,
    private val context: Context,
) : EvalContext {

    override fun process(left: Any?, operator: Operator, right: Any?): Boolean {
        return operator.process(this, left, right)
    }

    override fun engine(): Evaluator = evaluator

    /**
     * Return the Graal JS context.
     *
     * @return the Graal JS context.
     */
    fun context() = context
}
