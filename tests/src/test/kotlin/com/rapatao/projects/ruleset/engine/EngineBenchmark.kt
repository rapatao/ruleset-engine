package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.context.EvalEngine
import com.rapatao.projects.ruleset.engine.types.Expression
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTimedValue

fun main(args: Array<String>) {

    val evaluators = TestData.engines()
        .map { it.get().first { arg -> arg is EvalEngine } }
        .map { it as EvalEngine }
        .map { Evaluator(engine = it) }

    val cases = TestData.cases()
        .map { it.get().first { arg -> arg is Expression } }
        .map { it as Expression }

    // ini: warmup
    evaluators.forEach { evaluator ->
        println("warmup ${evaluator.engine().name()}: start")
        repeat(100) { cases.forEach { expression -> evaluator.evaluate(expression, TestData.inputData) } }
        println("warmup ${evaluator.engine().name()}: done")
    }
    // end: warmup

    val times = evaluators.associate {
        it.engine().name() to mutableListOf<Duration>()
    }.toMutableMap()

    val iterations = args.firstOrNull()?.let { Integer.parseInt(it) } ?: 1000

    println()

    evaluators.forEach { evaluator ->
        repeat(iterations) {
            val time =
                measureTimedValue {
                    cases.forEach { expression -> evaluator.evaluate(expression, TestData.inputData) }
                }

            print("\r${evaluator.engine().name()}: ${it + 1}")

            times[evaluator.engine().name()]?.add(time.duration)
        }
        println()
    }

    println()

    times.forEach { (engine, results) ->
        val total = results.reduce { acc, duration -> acc + duration }

        println("$engine> iterations: $iterations")
        println("    ops: " + (iterations * cases.size))
        println("  ops/s: " + ((iterations * cases.size) / total.toDouble(DurationUnit.SECONDS)))
        println("  total: $total")
        println("    max: " + results.max())
        println("    min: " + results.min())
        println("    avg: " + (total / results.size))

        val sortedResults = results.sorted()
        listOf(0.50, 0.75, 0.90, 0.95, 0.99).forEach { p ->
            println(
                "    p${(p * 100).toInt()}: " + sortedResults[(sortedResults.size * p).toInt()
                    .coerceAtMost(sortedResults.lastIndex)]
            )
        }

        println()
    }
}
