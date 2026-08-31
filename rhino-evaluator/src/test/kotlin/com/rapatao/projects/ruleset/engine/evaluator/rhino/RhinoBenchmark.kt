package com.rapatao.projects.ruleset.engine.evaluator.rhino

import com.rapatao.projects.ruleset.engine.BaseEngineBenchmark

fun main(args: Array<String>) {
    BaseEngineBenchmark(
        evaluator = RhinoEvaluator(),
        wide = args.getOrNull(1)?.toIntOrNull() ?: 0,
    ).main(args)
}
