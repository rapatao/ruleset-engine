package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import com.rapatao.projects.ruleset.engine.Evaluator
import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.Contains
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.EndsWith
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.Equals
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.GreaterOrEqualThan
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.GreaterThan
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.LessOrEqualThan
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.LessThan
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.NotEquals
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.StartsWith
import com.rapatao.projects.ruleset.engine.types.operators.Operator
import kotlin.reflect.full.memberProperties

/**
 * An evaluator engine implementation that uses Kotlin to process expressions.
 *
 * Supported types: Java primitive types, boolean, string, number types, maps, lists and arrays.
 */
open class KotlinEvaluator(
    operators: List<Operator> = listOf(),
) : Evaluator(
    operators = listOf(
        Equals(),
        NotEquals(),
        GreaterThan(),
        GreaterOrEqualThan(),
        LessThan(),
        LessOrEqualThan(),
        StartsWith(),
        EndsWith(),
        Contains(),
    ) + operators,
) {

    override fun <T> call(inputData: Any, block: (context: EvalContext) -> T): T {
        return block(KotlinContext(
            this,
            mutableMapOf<String, Any?>().apply {
                parseKeys("", inputData)
            }
        ))
    }

    override fun name(): String = "KotlinEval"

    private fun MutableMap<String, Any?>.parseKeys(node: String, input: Any?) {
        when {
            input.isValue() -> {
                this[node] = input
            }

            input is Collection<*> -> {
                this[node] = input

                input.forEachIndexed { index, value ->
                    parseKeys("${node}[$index]", value)
                }
            }

            input is Array<*> -> {
                @Suppress("DuplicatedCode")
                this[node] = input

                input.forEachIndexed { index, value ->
                    parseKeys("${node}[$index]", value)
                }
            }

            input is Map<*, *> -> {
                val currNode = node.childNode()

                input.forEach { key, value ->
                    this["${currNode}${key}"] = value
                    
                    parseKeys("${currNode}${key}", value)
                }
            }

            else -> {
                val currNode = node.childNode()

                input?.javaClass?.kotlin?.memberProperties?.forEach {
                    this["${currNode}${it.name}"] = it.get(input)

                    parseKeys("${currNode}${it.name}", it.get(input))
                }
            }
        }
    }

    private fun String.childNode() = if (this.isNotBlank()) "${this}." else this

    private fun Any?.isValue(): Boolean =
        this == null ||
            this.javaClass.isPrimitive ||
            this is Boolean ||
            this is String ||
            this is Number
}
