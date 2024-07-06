package com.rapatao.projects.ruleset.engine.types.operators

/**
 * Represents the less than or equal to operator (<=), used to compare if one value is less than or equal to
 * another.
 */
abstract class LessOrEqualThanOperator : Operator {
    override fun name(): String = BuiltInOperators.LESS_OR_EQUAL_THAN
}
