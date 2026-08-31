package com.rapatao.projects.ruleset.engine.evaluator.rhino.operator

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.ContainsOperator

internal class Contains : ContainsOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        context.evaluate(
            """
                (function(source, value) {
                    if (typeof source === 'string') {
                        return source.indexOf(value) !== -1
                    }
                    if (source == null || typeof source.length !== 'number') {
                        throw new TypeError('contains does not support ' + typeof source)
                    }
                    // Scanned rather than delegated to indexOf, because a java.util.List compares its elements by
                    // Java equality and a JS number never equals a boxed Integer under it.
                    for (var i = 0; i < source.length; i++) {
                        if (source[i] == value) {
                            return true
                        }
                    }
                    return false
                })($left, $right)
            """.trimIndent()
        )
}
