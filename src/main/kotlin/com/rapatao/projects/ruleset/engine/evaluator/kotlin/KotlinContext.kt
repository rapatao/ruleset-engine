package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.Parser.parse
import com.rapatao.projects.ruleset.engine.types.Expression
import java.math.BigDecimal

class KotlinContext(
    private val inputData: Map<String, Any?>
) : EvalContext {

    override fun process(expression: Expression): Boolean {
        val left = expression.left.asValue()
        val right = expression.right.asValue()

        return parse(expression, left, right)
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
