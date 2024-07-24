package com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.EndsWithOperator

internal class EndsWith : EndsWithOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        context.evaluate("($left).endsWith(($right))")
}
