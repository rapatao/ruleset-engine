package com.rapatao.projects.ruleset.engine.types.errors

/**
 * Throw when an expression is using an unrecognized operator
 */
class UnknownOperator(name: String) : RuntimeException(
    "Unknown operator: $name"
)
