package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import com.rapatao.projects.ruleset.engine.Evaluator
import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.Operator
import java.math.BigDecimal
import java.math.BigInteger

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

    private fun Any?.asValue(): Any? = this.resolved().normalized()

    /**
     * Numbers become `BigDecimal` so that an `Int` operand and a `BigDecimal` field compare as equal.
     *
     * A `BigDecimal` is already one and is kept as it is, fraction included. A `BigInteger` is converted directly,
     * because going through `toLong` overflows above `Long.MAX_VALUE`.
     *
     * A collection is normalized element by element, and only when it holds a number, so that
     * `listOf(1, 2) expContains 1` matches. The scan keeps a collection of non-numbers as it is, which is the
     * common case and the one on the hot path.
     */
    private fun Any?.normalized(): Any? = when {
        this is BigDecimal -> this
        this is BigInteger -> BigDecimal(this)
        this is Number && (this is Double || this is Float) -> BigDecimal(this.toDouble())
        this is Number && this !is Byte -> BigDecimal.valueOf(this.toLong())
        this is Collection<*> && this.any { it is Number || it is Collection<*> } -> this.map { it.normalized() }
        else -> this
    }

    private fun Any?.resolved(): Any? = when {
        // A list written in the expression holds operands, so each element is resolved on its own.
        this is Collection<*> -> this.map { it.resolved() }
        this !is String -> this
        this == "null" -> null
        else -> {
            val trimmed = this.trim()
            if (trimmed.isQuoted()) trimmed.unwrap() else trimmed.rawValue()
        }
    }

    /** A quoted literal, which a regex would have to match across newlines to recognize. */
    private fun String.isQuoted(): Boolean = this.length >= QUOTED_MIN && this[0] == '"' && this[this.length - 1] == '"'

    @Suppress("ReturnCount")
    private fun String.rawValue(): Any? {
        val key = this.unwrap()

        // A path never starts like a number, so the number parses, which scan the whole text, are skipped for it.
        if (key.startsLikeNumber()) {
            key.toBigIntegerOrNull()?.let { return it }
            key.toBigDecimalOrNull()?.let { return it }
        }

        key.toBooleanStrictOrNull()?.let { return it }

        val resolved = InputPath.resolve(inputData, key)

        if (resolved === InputPath.ABSENT) {
            throw NoSuchElementException("$key not found")
        }

        return resolved
    }

    private fun String.startsLikeNumber(): Boolean = this.isNotEmpty() && (this[0].isDigit() || this[0] in NUMBER_LEAD)

    private fun String.unwrap() = this.trim()
        .removePrefix("\"")
        .removeSuffix("\"")

    private companion object {
        /** `""` is the shortest quoted literal, so a single `"` is a path and not an empty one. */
        private const val QUOTED_MIN = 2

        /** The non-digit characters a number literal can start with. */
        private const val NUMBER_LEAD = "-+."
    }
}
