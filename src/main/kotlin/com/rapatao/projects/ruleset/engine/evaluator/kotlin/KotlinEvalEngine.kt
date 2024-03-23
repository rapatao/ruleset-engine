package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.context.EvalEngine
import kotlin.reflect.full.memberProperties

class KotlinEvalEngine : EvalEngine {
    override fun <T> call(inputData: Any, block: (context: EvalContext) -> T): T {
        val params: MutableMap<String, Any?> = mutableMapOf()

        parseKeys("", params, inputData)

        return block(KotlinContext(params))
    }

    override fun name(): String = "KotlinEval"

    private fun parseKeys(node: String, params: MutableMap<String, Any?>, input: Any?) {
        when {
            input.isValue() -> {
                params[node] = input
            }

            input is Collection<*> -> {
                params[node] = input

                input.forEachIndexed { index, value ->
                    parseKeys("${node}[${index}]", params, value)
                }
            }

            input is Array<*> -> {
                parseKeys(node, params, input)
            }

            input is Map<*, *> -> {
                val currNode = if (node.isNotBlank()) {
                    "${node}."
                } else {
                    node
                }
                input.forEach { key, value ->
                    parseKeys("${currNode}${key}", params, value)
                }
            }

            else -> {
                val currNode = if (node.isNotBlank()) {
                    "${node}."
                } else {
                    node
                }

                input?.javaClass?.kotlin?.memberProperties?.map {
                    parseKeys("${currNode}${it.name}", params, it.get(input))
                }
            }
        }
    }

    private fun Any?.isValue(): Boolean =
        this == null ||
            this.javaClass.isPrimitive ||
            this is Boolean ||
            this is String ||
            this is Number
}
