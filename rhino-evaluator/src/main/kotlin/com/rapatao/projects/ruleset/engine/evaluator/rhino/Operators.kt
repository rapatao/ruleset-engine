package com.rapatao.projects.ruleset.engine.evaluator.rhino

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.ContainsOperator
import com.rapatao.projects.ruleset.engine.types.operators.EndsWithOperator
import com.rapatao.projects.ruleset.engine.types.operators.EqualsOperator
import com.rapatao.projects.ruleset.engine.types.operators.GreaterOrEqualThanOperator
import com.rapatao.projects.ruleset.engine.types.operators.GreaterThanOperator
import com.rapatao.projects.ruleset.engine.types.operators.LessOrEqualThanOperator
import com.rapatao.projects.ruleset.engine.types.operators.LessThanOperator
import com.rapatao.projects.ruleset.engine.types.operators.NotEqualsOperator
import com.rapatao.projects.ruleset.engine.types.operators.StartsWithOperator

internal class Equals : EqualsOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        context.evaluate("($left) == ($right)")
}

internal class NotEquals : NotEqualsOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        context.evaluate("($left) != ($right)")
}

internal class GreaterThan : GreaterThanOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        context.evaluate("($left) > ($right)")
}

internal class GreaterOrEqualThan : GreaterOrEqualThanOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        context.evaluate("($left) >= ($right)")
}

internal class LessThan : LessThanOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        context.evaluate("($left) < ($right)")
}

internal class LessOrEqualThan : LessOrEqualThanOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        context.evaluate("($left) <= ($right)")
}

internal class StartsWith : StartsWithOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        context.evaluate("($left).startsWith(($right))")
}

internal class EndsWith : EndsWithOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        context.evaluate("($left).endsWith(($right))")
}

internal class Contains : ContainsOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        context.evaluate(
            """
                (function() {
                    if (Array.isArray(${left})) {
                        return ${left}.includes(${right})
                    } else {
                        return ${left}.indexOf(${right}) !== -1
                    }
                })()
            """.trimIndent()
        )
}

private fun EvalContext.evaluate(content: String): Boolean {
    val rhinoContext = this as RhinoContext

    return true == rhinoContext.context()
        .compileString(
            "true == ($content)",
            content,
            0,
            null,
        ).exec(
            rhinoContext.context(),
            rhinoContext.scope(),
        )
}
