package com.rapatao.projects.ruleset.engine.evaluator.rhino.operator

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.NotContainsOperator

internal class NotContains : NotContainsOperator() {
    private val containsOperator = Contains()

    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        !containsOperator.process(context, left, right)
}
