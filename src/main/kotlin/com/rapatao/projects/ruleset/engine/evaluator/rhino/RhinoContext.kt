package com.rapatao.projects.ruleset.engine.evaluator.rhino

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure
import org.mozilla.javascript.Context
import org.mozilla.javascript.Script
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

    /**
     * Processes an expression.
     *
     * @param expression the expression to process
     * @return true if the expression is successfully processed, false otherwise
     * @throws Exception if the expression processing fails and onFailure is set to THROW
     */
    override fun process(expression: Expression): Boolean {
        return try {
            true == expression.asScript(context)
                .exec(context, scope)
        } catch (@SuppressWarnings("TooGenericExceptionCaught") e: Exception) {
            when (expression.onFailure) {
                OnFailure.TRUE -> true
                OnFailure.FALSE -> false
                OnFailure.THROW -> throw e
            }
        }
    }

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

    private fun Expression.asScript(context: Context): Script {
        val script = Parser.parse(this)

        return context.compileString(
            "true == ($script)",
            script,
            0,
            null,
        )
    }
}
