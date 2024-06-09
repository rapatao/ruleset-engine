package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.context.EvalEngine
import com.rapatao.projects.ruleset.engine.types.Expression
import java.nio.file.Paths
import kotlin.io.path.appendText
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.writeText
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTimedValue

class BaseEngineBenchmark(private val evalEngine: EvalEngine) {

    private val benchOut = Paths.get("bench_${evalEngine.name()}.txt")

    @Suppress("MagicNumber")
    fun main(args: Array<String>) {

        cleanup()

        val cases = TestData.cases()
            .map { it.get().first { arg -> arg is Expression } }
            .map { it as Expression }

        val evaluator = Evaluator(engine = evalEngine)

        // ini: warmup
        appendLine("warmup ${evaluator.engine().name()}: start")
        repeat(100) { cases.forEach { expression -> evaluator.evaluate(expression, TestData.inputData) } }
        appendLine("warmup ${evaluator.engine().name()}: done")
        // end: warmup

        val times = mutableListOf<Duration>()

        val iterations = args.firstOrNull()?.let { Integer.parseInt(it) } ?: 1000

        appendLine()

        repeat(iterations) {
            val time =
                measureTimedValue {
                    cases.forEach { expression -> evaluator.evaluate(expression, TestData.inputData) }
                }

            print("\r${evaluator.engine().name()}: ${it + 1}")

            times.add(time.duration)
        }
        appendLine()

        appendLine()

        val total = times.reduce { acc, duration -> acc + duration }

        appendLine("$evalEngine> iterations: $iterations")
        appendLine("    ops: " + (iterations * cases.size))
        appendLine("  ops/s: " + ((iterations * cases.size) / total.toDouble(DurationUnit.SECONDS)))
        appendLine("  total: $total")
        appendLine("    max: " + times.max())
        appendLine("    min: " + times.min())
        appendLine("    avg: " + (total / times.size))

        val sortedResults = times.sorted()
        listOf(0.50, 0.75, 0.90, 0.95, 0.99).forEach { p ->
            appendLine(
                "    p${(p * 100).toInt()}: " + sortedResults[(sortedResults.size * p).toInt()
                    .coerceAtMost(sortedResults.lastIndex)]
            )
        }

        appendLine()
    }

    private fun append(value: String) {
        benchOut.appendText(value)
        print(value)
    }

    private fun appendLine(value: String? = "") {
        append(value + "\n")
    }

    private fun cleanup() {
        if (!benchOut.exists()) {
            benchOut.createFile()
        }
        benchOut.writeText("")
    }
}
