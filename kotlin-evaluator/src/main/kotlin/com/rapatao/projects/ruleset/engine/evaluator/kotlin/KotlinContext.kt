package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.Operator
import java.math.BigDecimal

/**
 * KotlinContext is a class that implements the EvalContext interface.
 * It provides the ability to process expressions using Kotlin operations.
 *
 * @param inputData the map containing the input data to be used during expression evaluation
 */
class KotlinContext(
    private val inputData: Map<String, Any?>
) : EvalContext {

    override fun process(left: Any?, operator: Operator, right: Any?): Boolean {
        return operator.process(this, left.asValue(), right.asValue())
    }

    private fun Any?.asValue(): Any? {
        val result = when {
            this is String && !this.trim().matches(Regex("^\".*\"$")) -> rawValue()
            this is String && this.trim().matches(Regex("\".*\"")) -> this.unwrap()
            else -> this
        }

        return when {
            result is Number && (result is Double || result is Float) -> BigDecimal(result.toDouble())
            result is Number && result !is Byte -> BigDecimal.valueOf(result.toLong())
            else -> result
        }
    }

    private fun String.rawValue(): Any? {
        val key = this.unwrap()

        return listOf(
            {
                key.toBigIntegerOrNull()
            },
            {
                key.toBigDecimalOrNull()
            },
            {
                key.toBooleanStrictOrNull()
            },
            {
                inputData.getOrElse(key) {
                    throw NoSuchElementException("$key not found")
                }
            },
        ).firstNotNullOfOrNull { it() }
    }

    private fun String.unwrap() = this.trim()
        .replace(Regex("^\""), "")
        .replace(Regex("\"$"), "")
}
