package com.rapatao.projects.ruleset.engine.evaluator.graaljs

import com.rapatao.projects.ruleset.engine.BaseEngineBenchmark

fun main(args: Array<String>) {
    BaseEngineBenchmark(
        GraalJSEvaluator(reuseContextPerThread = args.getOrNull(1).toBoolean())
    ).main(args)
}
