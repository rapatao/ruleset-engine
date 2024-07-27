package com.rapatao.projects.ruleset.engine.types.operators

/**
 * Represents the operation to check if a string not starts with a specified sequence of characters.
 */
abstract class NotStartsWithOperator : Operator {
    override fun name(): String = BuiltInOperators.NOT_STARTS_WITH
}
