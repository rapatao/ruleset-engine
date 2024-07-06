package com.rapatao.projects.ruleset.engine.types.operators

/**
 * Represents the operation to check if a string contains a specified sequence of characters or if an array contains
 * a particular element.
 */
abstract class ContainsOperator : Operator {
    override fun name(): String = BuiltInOperators.CONTAINS
}
