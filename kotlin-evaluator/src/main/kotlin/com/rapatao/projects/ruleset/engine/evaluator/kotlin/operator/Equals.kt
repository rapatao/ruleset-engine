package com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.EqualsOperator

internal class Equals : EqualsOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        left == right
}
