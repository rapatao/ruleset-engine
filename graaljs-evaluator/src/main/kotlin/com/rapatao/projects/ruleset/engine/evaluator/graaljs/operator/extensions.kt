package com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.GraalJSContext
import org.graalvm.polyglot.Source

internal fun EvalContext.evaluate(content: String): Boolean {
    val graalJSContext = this as GraalJSContext

    return graalJSContext.context().eval(
        Source.newBuilder(
            "js",
            "true == ($content)",
            content,
        ).buildLiteral()
    ).asBoolean()
}
