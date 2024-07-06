package com.rapatao.projects.ruleset.engine.evaluator.rhino

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.Operator
import org.mozilla.javascript.Context
import org.mozilla.javascript.ScriptableObject

/**
 * RhinoContext is a class that implements the EvalContext interface.
 * It provides the ability to process expressions using the Rhino JavaScript engine.
 *
 * @property context The Rhino Context object.
 * @property scope The ScriptableObject representing the scope in which the expressions will be executed.
 */
class RhinoContext(
    private val context: Context,
    private val scope: ScriptableObject,
) : EvalContext {

    override fun process(left: Any?, operator: Operator, right: Any?): Boolean =
        operator.process(this, left, right)

    /**
     * Returns the Rhino context.
     *
     * @return The Rhino context.
     */
    fun context() = context

    /**
     * Returns the Rhino scope.
     *
     * @return The Rhino scope.
     */
    fun scope() = scope
}
