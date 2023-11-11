package com.rapatao.projects.ruleset.engine.context

interface EvalEngine {
    fun <T> call(inputData: Any, block: (context: EvalContext) -> T): T
}
