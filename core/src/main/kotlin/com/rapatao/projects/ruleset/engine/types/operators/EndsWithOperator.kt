package com.rapatao.projects.ruleset.engine.types.operators

/**
 * Represents the operation to check if a string ends with a specified sequence of characters.
 */
abstract class EndsWithOperator : Operator {
    override fun name(): String = BuiltInOperators.ENDS_WITH
}
