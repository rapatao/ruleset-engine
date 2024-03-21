package com.rapatao.projects.ruleset.engine.context

interface EvalEngine {
    fun <T> call(inputData: Any, block: EvalContext.() -> T): T
}
