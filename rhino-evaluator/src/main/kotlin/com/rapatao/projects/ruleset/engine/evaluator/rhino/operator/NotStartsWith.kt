package com.rapatao.projects.ruleset.engine.evaluator.rhino.operator

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.NotStartsWithOperator

internal class NotStartsWith : NotStartsWithOperator() {
    private val startsWithOperator = StartsWith()

    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        !startsWithOperator.process(context, left, right)
}
