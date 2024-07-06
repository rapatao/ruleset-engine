package com.rapatao.projects.ruleset.engine.types.operators

/**
 * Represents the less than operator (<), used to compare if one value is less than another.
 */
abstract class LessThanOperator : Operator {
    override fun name(): String = BuiltInOperators.LESS_THAN
}
