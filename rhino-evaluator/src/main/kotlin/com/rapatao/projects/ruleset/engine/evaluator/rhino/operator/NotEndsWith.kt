package com.rapatao.projects.ruleset.engine.evaluator.rhino.operator

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.NotEndsWithOperator

internal class NotEndsWith : NotEndsWithOperator() {
    private val endsWithOperator = EndsWith()

    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        !endsWithOperator.process(context, left, right)
}
