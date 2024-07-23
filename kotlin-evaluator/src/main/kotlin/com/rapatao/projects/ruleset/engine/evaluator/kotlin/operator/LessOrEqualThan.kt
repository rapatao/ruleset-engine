package com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.LessOrEqualThanOperator

internal class LessOrEqualThan : LessOrEqualThanOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        left.comparable() <= right
}
