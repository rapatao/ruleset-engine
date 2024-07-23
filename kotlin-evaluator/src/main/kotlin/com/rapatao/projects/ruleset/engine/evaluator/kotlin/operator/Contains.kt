package com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.ContainsOperator

internal class Contains : ContainsOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        left.checkContains(right)


    private fun Any?.checkContains(value: Any?): Boolean {
        return when {
            this is String && value is String -> this.contains(value)
            this is Collection<*> -> this.contains(value)
            this is Array<*> -> this.contains(value)
            else -> throw UnsupportedOperationException("contains doesn't support ${this?.javaClass} type")
        }
    }
}
