package com.rapatao.projects.ruleset.engine.types

data class Matcher(
    val allMatch: List<Matcher>? = null,
    val anyMatch: List<Matcher>? = null,
    val noneMatch: List<Matcher>? = null,
    val expression: Expression? = null,
)
