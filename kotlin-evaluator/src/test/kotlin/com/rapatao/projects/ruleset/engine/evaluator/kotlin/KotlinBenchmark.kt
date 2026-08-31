package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import com.rapatao.projects.ruleset.engine.BaseEngineBenchmark

fun main(args: Array<String>) {
    BaseEngineBenchmark(
        evaluator = KotlinEvaluator(),
        wide = args.getOrNull(1)?.toIntOrNull() ?: 0,
    ).main(args)
}
