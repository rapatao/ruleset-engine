package com.rapatao.projects.ruleset.engine.types.operators

/**
 * Represents the operation to check if a string not contains a specified sequence of characters or if an array
 * not contains a particular element.
 */
abstract class NotContainsOperator : Operator {
    override fun name(): String = BuiltInOperators.NOT_CONTAINS
}
