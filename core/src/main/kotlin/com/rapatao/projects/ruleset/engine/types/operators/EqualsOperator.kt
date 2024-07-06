package com.rapatao.projects.ruleset.engine.types.operators

/**
 * Represents the equality operator (==), used to check if two values are equal.
 */
abstract class EqualsOperator : Operator {
    override fun name(): String = "equals"
}
