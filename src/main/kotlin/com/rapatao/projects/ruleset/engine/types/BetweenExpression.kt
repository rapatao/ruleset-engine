package com.rapatao.projects.ruleset.engine.types

data class BetweenExpression(
    val left: Any,
    val from: Any,
    val to: Any
) : Expression {
    override fun parse(): String = "$left >= $from && $left <= $to"
}
