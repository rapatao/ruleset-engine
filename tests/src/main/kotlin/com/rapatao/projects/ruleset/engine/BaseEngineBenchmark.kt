package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.types.Expression
import java.lang.management.ManagementFactory
import java.nio.file.Paths
import kotlin.io.path.appendText
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.writeText
import kotlin.math.sqrt
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTimedValue
import com.sun.management.ThreadMXBean as HotSpotThreadMXBean

/**
 * Replays the full rule set from [TestData] against one engine.
 *
 * Every `bench` task passes the same arguments in the same order, and each benchmark reads the ones its engine
 * supports:
 *
 * | index | property           | meaning                                                |
 * |-------|--------------------|--------------------------------------------------------|
 * | 0     | `benchIterations`  | iterations of the whole rule set                       |
 * | 1     | `benchWide`        | extra fields and list elements around the input, 0 off |
 * | 2     | `benchReuse`       | GraalJS only, `reuseContextPerThread`                  |
 *
 * Reported per run: throughput and the latency distribution of an iteration, the bytes each engine allocates per
 * evaluation, and the garbage collections that happened while the measured loop ran.
 */
class BaseEngineBenchmark(
    private val evaluator: Evaluator,
    private val wide: Int = 0,
) {

    private val benchOut = Paths.get("bench_${evaluator.name()}.txt")

    // Both roots are maps, so the wide run differs from the default one by width alone.
    private val input: Any = if (wide > 0) TestData.wideInput(wide) else TestData.narrowInput

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

        val before = usage()

        repeat(iterations) {
            val time =
                measureTimedValue {
                    cases.forEach { expression -> evaluator.evaluate(expression, input) }
                }

            print("\r${evaluator.name()}: ${it + 1}")

            times.add(time.duration)
        }

        val consumed = usage() - before

        appendLine()
        appendLine()

        report(iterations = iterations, perIteration = cases.size, times = times, consumed = consumed)
    }

    @Suppress("MagicNumber")
    private fun report(iterations: Int, perIteration: Int, times: List<Duration>, consumed: Usage) {
        val total = times.reduce { acc, duration -> acc + duration }
        val ops = iterations.toLong() * perIteration

        appendLine("${evaluator.name()}> iterations: $iterations")
        appendLine("      ops: $ops")
        appendLine("    ops/s: " + (ops / total.toDouble(DurationUnit.SECONDS)))
        appendLine("    total: $total")
        appendLine("      max: " + times.max())
        appendLine("      min: " + times.min())
        appendLine("      avg: " + (total / times.size))
        appendLine("   stddev: " + stdDev(times, total / times.size))

        val allocated = consumed.allocatedBytes

        appendLine(" alloc/op: " + if (allocated < 0) "n/a" else "%,d B".format(allocated / ops))
        appendLine("    alloc: " + if (allocated < 0) "n/a" else "%,.1f MB".format(allocated / MB))
        appendLine("       gc: ${consumed.gcCollections} collections, ${consumed.gcMillis}ms")

        val sorted = times.sorted()
        listOf(0.50, 0.75, 0.90, 0.95, 0.99).forEach { p ->
            appendLine(
                "      p${(p * 100).toInt()}: " + sorted[(sorted.size * p).toInt().coerceAtMost(sorted.lastIndex)]
            )
        }

        appendLine()
    }

    private fun stdDev(times: List<Duration>, avg: Duration): Duration {
        val mean = avg.toDouble(DurationUnit.MICROSECONDS)
        val variance = times.sumOf {
            val diff = it.toDouble(DurationUnit.MICROSECONDS) - mean
            diff * diff
        } / times.size

        return Duration.parse("${sqrt(variance)}us")
    }

    /**
     * Allocation is counted on this thread only, which is where every engine runs the evaluation, and garbage
     * collections are counted JVM wide.
     */
    private fun usage(): Usage {
        val gc = ManagementFactory.getGarbageCollectorMXBeans()
            .fold(0L to 0L) { acc, bean ->
                acc.first + bean.collectionCount.coerceAtLeast(0) to acc.second + bean.collectionTime.coerceAtLeast(0)
            }

        return Usage(
            allocatedBytes = allocatedBytes(),
            gcCollections = gc.first,
            gcMillis = gc.second,
        )
    }

    private fun allocatedBytes(): Long {
        val bean = (ManagementFactory.getThreadMXBean() as? HotSpotThreadMXBean)
            ?.takeIf { it.isThreadAllocatedMemorySupported }
            ?: return UNSUPPORTED

        if (!bean.isThreadAllocatedMemoryEnabled) {
            bean.isThreadAllocatedMemoryEnabled = true
        }

        return bean.getThreadAllocatedBytes(Thread.currentThread().threadId())
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

    private data class Usage(
        val allocatedBytes: Long,
        val gcCollections: Long,
        val gcMillis: Long,
    ) {
        operator fun minus(other: Usage) = Usage(
            allocatedBytes = if (allocatedBytes < 0 || other.allocatedBytes < 0) {
                UNSUPPORTED
            } else {
                allocatedBytes - other.allocatedBytes
            },
            gcCollections = gcCollections - other.gcCollections,
            gcMillis = gcMillis - other.gcMillis,
        )
    }

    private companion object {
        private const val UNSUPPORTED = -1L
        private const val MB = 1024.0 * 1024.0
    }
}
