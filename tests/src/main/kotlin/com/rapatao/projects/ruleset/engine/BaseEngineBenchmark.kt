package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.context.EvalEngine
import com.rapatao.projects.ruleset.engine.types.Expression
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTimedValue

class BaseEngineBenchmark(private val evalEngine: EvalEngine) {
    @Suppress("MagicNumber")
    fun main(args: Array<String>) {

        val cases = TestData.cases()
            .map { it.get().first { arg -> arg is Expression } }
            .map { it as Expression }

        val evaluator = Evaluator(engine = evalEngine)

        // ini: warmup
        println("warmup ${evaluator.engine().name()}: start")
        repeat(100) { cases.forEach { expression -> evaluator.evaluate(expression, TestData.inputData) } }
        println("warmup ${evaluator.engine().name()}: done")
        // end: warmup

        val times = mutableListOf<Duration>()

        val iterations = args.firstOrNull()?.let { Integer.parseInt(it) } ?: 1000

        println()

        repeat(iterations) {
            val time =
                measureTimedValue {
                    cases.forEach { expression -> evaluator.evaluate(expression, TestData.inputData) }
                }

            print("\r${evaluator.engine().name()}: ${it + 1}")

            times.add(time.duration)
        }
        println()

        println()

        val total = times.reduce { acc, duration -> acc + duration }

        println("$evalEngine> iterations: $iterations")
        println("    ops: " + (iterations * cases.size))
        println("  ops/s: " + ((iterations * cases.size) / total.toDouble(DurationUnit.SECONDS)))
        println("  total: $total")
        println("    max: " + times.max())
        println("    min: " + times.min())
        println("    avg: " + (total / times.size))

        val sortedResults = times.sorted()
        listOf(0.50, 0.75, 0.90, 0.95, 0.99).forEach { p ->
            println(
                "    p${(p * 100).toInt()}: " + sortedResults[(sortedResults.size * p).toInt()
                    .coerceAtMost(sortedResults.lastIndex)]
            )
        }

        println()
    }
}
