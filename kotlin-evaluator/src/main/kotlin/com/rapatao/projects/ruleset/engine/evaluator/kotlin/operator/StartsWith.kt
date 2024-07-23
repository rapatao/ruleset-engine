package com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.StartsWithOperator

internal class StartsWith : StartsWithOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        left.toString().startsWith(right.toString())
}
