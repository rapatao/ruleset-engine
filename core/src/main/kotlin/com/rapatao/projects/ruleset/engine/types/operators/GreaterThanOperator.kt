package com.rapatao.projects.ruleset.engine.types.operators

/**
 * Represents the greater than operator (>), used to compare if one value is greater than another.
 */
abstract class GreaterThanOperator : Operator {
    override fun name(): String = BuiltInOperators.GREATER_THAN
}
