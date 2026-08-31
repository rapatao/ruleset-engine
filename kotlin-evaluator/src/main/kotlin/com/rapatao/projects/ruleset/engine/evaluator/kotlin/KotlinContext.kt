package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import com.rapatao.projects.ruleset.engine.Evaluator
import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.Operator
import java.math.BigDecimal

/**
 * KotlinContext is a class that implements the EvalContext interface.
 * It provides the ability to process expressions using Kotlin operations.
 *
 * Operand paths are resolved by [InputPath] on demand, so only the nodes a path names are visited.
 *
 * @param evaluator the evaluator implementation instance
 * @param inputData the input data to be used during expression evaluation
 */
class KotlinContext(
    private val evaluator: Evaluator,
    private val inputData: Any
) : EvalContext {

    override fun process(left: Any?, operator: Operator, right: Any?): Boolean {
        return operator.process(this, left.asValue(), right.asValue())
    }

    override fun engine(): Evaluator = evaluator

    private fun Any?.asValue(): Any? {
        val result = this.resolved()

        return when {
            result is Number && (result is Double || result is Float) -> BigDecimal(result.toDouble())
            result is Number && result !is Byte -> BigDecimal.valueOf(result.toLong())
            else -> result
        }
    }

    private fun Any?.resolved(): Any? = when {
        // A list written in the expression holds operands, so each element is resolved on its own. Elements keep
        // their own type, as the elements of a list read from the input data do.
        this is Collection<*> -> this.map { it.resolved() }
        this !is String -> this
        this == "null" -> null
        else -> {
            val trimmed = this.trim()
            if (QUOTED.matches(trimmed)) trimmed.unwrap() else trimmed.rawValue()
        }
    }

    @Suppress("ReturnCount")
    private fun String.rawValue(): Any? {
        val key = this.unwrap()

        key.toBigIntegerOrNull()?.let { return it }
        key.toBigDecimalOrNull()?.let { return it }
        key.toBooleanStrictOrNull()?.let { return it }

        val resolved = InputPath.resolve(inputData, key)

        if (resolved === InputPath.ABSENT) {
            throw NoSuchElementException("$key not found")
        }

        return resolved
    }

    private fun String.unwrap() = this.trim()
        .removePrefix("\"")
        .removeSuffix("\"")

    private companion object {
        // Regex.matches is a full-input match, so no anchors are needed.
        private val QUOTED = Regex("\".*\"")
    }
}
