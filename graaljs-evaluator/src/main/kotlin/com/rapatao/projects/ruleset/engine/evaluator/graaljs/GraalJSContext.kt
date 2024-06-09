package com.rapatao.projects.ruleset.engine.evaluator.graaljs

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.Expression
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.Source

/**
 * GraalJSContext is a class that implements the EvalContext interface.
 * It provides the ability to process expressions using the Graal JS engine.
 *
 * @property context the GraalJS context object.
 */
class GraalJSContext(
    private val context: Context,
) : EvalContext {

    /**
     * Processes an expression.
     *
     * @param expression the expression to process
     * @return true if the expression is successfully processed, false otherwise
     * @throws Exception if the expression processing fails and onFailure is set to THROW
     */
    override fun process(expression: Expression): Boolean {
        return context.eval(expression.asScript()).asBoolean()
    }

    /**
     * Return the Graal JS context.
     *
     * @return the Graal JS context.
     */
    fun context() = context

    private fun Expression.asScript(): Source {
        val script = Parser.parse(this)

        return Source.newBuilder(
            "js",
            "true == ($script)",
            script
        ).buildLiteral()
    }
}
