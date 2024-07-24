package com.rapatao.projects.ruleset.engine.evaluator.rhino.operator

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.ContainsOperator

internal class Contains : ContainsOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        context.evaluate(
            """
                (function() {
                    if (Array.isArray(${left})) {
                        return ${left}.includes(${right})
                    } else {
                        return ${left}.indexOf(${right}) !== -1
                    }
                })()
            """.trimIndent()
        )
}
