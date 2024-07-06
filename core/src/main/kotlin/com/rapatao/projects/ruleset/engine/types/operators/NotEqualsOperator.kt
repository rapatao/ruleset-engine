package com.rapatao.projects.ruleset.engine.types.operators

/**
 * Represents the inequality operator (!=), used to check if two values are not equal.
 */
abstract class NotEqualsOperator : Operator {
    override fun name(): String = "not_equals"
}
