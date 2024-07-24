package com.rapatao.projects.ruleset.engine.evaluator.rhino.operator

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.NotEqualsOperator

internal class NotEquals : NotEqualsOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        context.evaluate("($left) != ($right)")
}
