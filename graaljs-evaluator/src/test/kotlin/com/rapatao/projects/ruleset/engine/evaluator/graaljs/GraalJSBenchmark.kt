package com.rapatao.projects.ruleset.engine.evaluator.graaljs

import com.rapatao.projects.ruleset.engine.BaseEngineBenchmark

fun main(args: Array<String>) {
    BaseEngineBenchmark(
        evaluator = GraalJSEvaluator(reuseContextPerThread = args.getOrNull(2).toBoolean()),
        wide = args.getOrNull(1)?.toIntOrNull() ?: 0,
    ).main(args)
}
