package com.rapatao.projects.ruleset.engine.evaluator.rhino.operator

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.evaluator.rhino.RhinoContext

internal fun EvalContext.evaluate(content: String): Boolean {
    val rhinoContext = this as RhinoContext

    return true == rhinoContext.context()
        .compileString(
            "true == ($content)",
            content,
            0,
            null,
        ).exec(
            rhinoContext.context(),
            rhinoContext.scope(),
        )
}
