package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.Operator.CONTAINS
import com.rapatao.projects.ruleset.engine.types.Operator.ENDS_WITH
import com.rapatao.projects.ruleset.engine.types.Operator.EQUALS
import com.rapatao.projects.ruleset.engine.types.Operator.GREATER_OR_EQUAL_THAN
import com.rapatao.projects.ruleset.engine.types.Operator.GREATER_THAN
import com.rapatao.projects.ruleset.engine.types.Operator.LESS_OR_EQUAL_THAN
import com.rapatao.projects.ruleset.engine.types.Operator.LESS_THAN
import com.rapatao.projects.ruleset.engine.types.Operator.NOT_EQUALS
import com.rapatao.projects.ruleset.engine.types.Operator.STARTS_WITH
import java.math.BigDecimal

class KotlinContext(
    private val inputData: Map<String, Any?>
) : EvalContext {

    override fun process(expression: Expression): Boolean {
        val left = expression.left.asValue()
        val right = expression.right.asValue()

        return try {
            when (expression.operator) {
                EQUALS -> left == right
                NOT_EQUALS -> left != right
                CONTAINS -> left.checkContains(right)
                else -> {
                    if (left is Comparable<*>) {
                        @Suppress("UNCHECKED_CAST")
                        return when (expression.operator) {
                            GREATER_THAN -> left as Comparable<Any?> > right
                            GREATER_OR_EQUAL_THAN -> left as Comparable<Any?> >= right
                            LESS_THAN -> left as Comparable<Any?> < right
                            LESS_OR_EQUAL_THAN -> left as Comparable<Any?> <= right
                            STARTS_WITH -> left.toString().startsWith(right.toString())
                            ENDS_WITH -> left.toString().endsWith(right.toString())
                            else -> throw UnsupportedOperationException("operation not supported")
                        }
                    } else {
                        throw UnsupportedOperationException("operation not supported")
                    }
                }
            }
        } catch (@SuppressWarnings("TooGenericExceptionCaught") e: Exception) {
            when (expression.onFailure) {
                OnFailure.TRUE -> true
                OnFailure.FALSE -> false
                OnFailure.THROW -> throw e
            }
        }
    }

    private fun Any?.checkContains(value: Any?): Boolean {
        return when {
            this is String && value is String -> this.contains(value)
            this is Collection<*> -> this.contains(value)
            this is Array<*> -> this.contains(value)
            else -> throw UnsupportedOperationException("contains doesn't support ${this?.javaClass} type")
        }
    }

    private fun Any?.asValue(): Any? {
        val result = when {
            this is String && !this.trim().matches(Regex("^\".*\"$")) -> rawValue()
            this is String && this.trim().matches(Regex("\".*\"")) -> this.unwrap()
            else -> this
        }

        if (result is Number && (result is Double || result is Float)) {
            return BigDecimal(result.toDouble())
        }
        if (result is Number && result !is Byte) {
            return BigDecimal.valueOf(result.toLong())
        }

        return result
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
            }
        ).firstNotNullOf { it() }
    }

    private fun String.unwrap() = this.trim()
        .replace(Regex("^\""), "")
        .replace(Regex("\"$"), "")
}
