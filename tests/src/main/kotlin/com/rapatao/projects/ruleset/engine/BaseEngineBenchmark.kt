package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.types.Expression
import java.nio.file.Paths
import kotlin.io.path.appendText
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.writeText
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTimedValue

/**
 * Replays the full rule set from [TestData] against one engine.
 *
 * Every `bench` task passes the same arguments in the same order, and each benchmark reads the ones its engine
 * supports:
 *
 * | index | property           | meaning                                              |
 * |-------|--------------------|------------------------------------------------------|
 * | 0     | `benchIterations`  | iterations of the whole rule set                     |
 * | 1     | `benchWide`        | extra fields and list elements around the input, 0 off |
 * | 2     | `benchReuse`       | GraalJS only, `reuseContextPerThread`                |
 */
class BaseEngineBenchmark(
    private val evaluator: Evaluator,
    private val wide: Int = 0,
) {

    private val benchOut = Paths.get("bench_${evaluator.name()}.txt")

    private val input = if (wide > 0) TestData.wideInput(wide) else TestData.inputData

    @Suppress("MagicNumber")
    fun main(args: Array<String>) {

        cleanup()

        val cases = TestData.cases()
            .map { it.get().first { arg -> arg is Expression } }
            .map { it as Expression }

        appendLine("${evaluator.name()}> input: " + if (wide > 0) "wide($wide)" else "default")

        // ini: warmup
        appendLine("warmup ${evaluator.name()}: start")
        repeat(100) { cases.forEach { expression -> evaluator.evaluate(expression, input) } }
        appendLine("warmup ${evaluator.name()}: done")
        // end: warmup

        val times = mutableListOf<Duration>()

        val iterations = args.firstOrNull()?.let { Integer.parseInt(it) } ?: 1000

        appendLine()

        repeat(iterations) {
            val time =
                measureTimedValue {
                    cases.forEach { expression -> evaluator.evaluate(expression, input) }
                }

            print("\r${evaluator.name()}: ${it + 1}")

            times.add(time.duration)
        }
        appendLine()

        appendLine()

        val total = times.reduce { acc, duration -> acc + duration }

        appendLine("${evaluator.name()}> iterations: $iterations")
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
