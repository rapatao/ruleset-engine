package com.rapatao.projects.ruleset.engine.types.errors

class UnknownOperator(name: String) : RuntimeException(
    "Unknown operator: $name"
)
