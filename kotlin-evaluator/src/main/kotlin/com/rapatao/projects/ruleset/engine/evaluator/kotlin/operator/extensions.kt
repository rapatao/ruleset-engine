package com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator

import java.math.BigDecimal

@Suppress("UNCHECKED_CAST")
internal fun <T> T.comparable() = this as Comparable<T>

/**
 * Equality that compares numbers by value.
 *
 * Every number reaches an operator as a `BigDecimal`, and `BigDecimal.equals` is scale sensitive, so `10` and `10.00`
 * are not equal to it while `compareTo` reports them as the same number. A collection is compared element by element
 * under the same rule.
 */
internal fun Any?.matches(other: Any?): Boolean = when {
    this is BigDecimal && other is BigDecimal -> this.compareTo(other) == 0
    this is Collection<*> && other is Collection<*> ->
        this.size == other.size && this.asSequence().zip(other.asSequence()).all { (a, b) -> a.matches(b) }

    else -> this == other
}
