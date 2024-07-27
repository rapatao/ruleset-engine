package com.rapatao.projects.ruleset.engine.helper

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.Operator

class ExposeEngineTestOperator : Operator {
    companion object {
        const val EXPOSED_OPERATOR_NAME = "engine_is_exposed"
    }

    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        left.isEquals(context.engine().name()) && right.isEquals(context.engine().name())

    private fun Any?.isEquals(expected: String): Boolean =
        // necessary because operator doesn't consider implementation variable handling
        this == expected || this == "\"$expected\""

    override fun name(): String = EXPOSED_OPERATOR_NAME
}
