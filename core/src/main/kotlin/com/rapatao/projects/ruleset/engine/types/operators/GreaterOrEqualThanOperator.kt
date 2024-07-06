package com.rapatao.projects.ruleset.engine.types.operators

/**
 * Represents the greater than or equal to operator (>=), used to compare if one value is greater than or equal to
 * another.
 */
abstract class GreaterOrEqualThanOperator : Operator {
    override fun name(): String = "greater_or_equal_than"
}
