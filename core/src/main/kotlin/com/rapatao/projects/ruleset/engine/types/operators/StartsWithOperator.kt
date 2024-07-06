package com.rapatao.projects.ruleset.engine.types.operators

/**
 * Represents the operation to check if a string starts with a specified sequence of characters.
 */
abstract class StartsWithOperator : Operator {
    override fun name(): String = "starts_with"
}
