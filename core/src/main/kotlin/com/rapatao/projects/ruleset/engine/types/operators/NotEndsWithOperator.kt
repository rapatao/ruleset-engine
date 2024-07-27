package com.rapatao.projects.ruleset.engine.types.operators

/**
 * Represents the operation to check if a string not ends with a specified sequence of characters.
 */
abstract class NotEndsWithOperator : Operator {
    override fun name(): String = BuiltInOperators.NOT_ENDS_WITH
}
