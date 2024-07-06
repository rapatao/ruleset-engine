package com.rapatao.projects.ruleset.engine.types.operators

/**
 * Represents the less than or equal to operator (<=), used to compare if one value is less than or equal to
 * another.
 */
abstract class LessOrEqualThanOperator : Operator {
    override fun name(): String = "less_or_equal_than"
}
