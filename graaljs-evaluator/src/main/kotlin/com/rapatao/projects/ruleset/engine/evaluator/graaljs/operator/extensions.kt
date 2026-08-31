package com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.GraalJSContext
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.GraalJSEvaluator.Companion.INPUT_SCOPE
import org.graalvm.polyglot.Source

internal fun EvalContext.evaluate(content: String): Boolean {
    val graalJSContext = this as GraalJSContext

    return graalJSContext.context().eval(
        Source.newBuilder(
            "js",
            "(function() { with ($INPUT_SCOPE) { return true == ($content) } })()",
            content,
        ).buildLiteral()
    ).asBoolean()
}
