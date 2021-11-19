package com.rapatao.projects.ruleset.engine.types

data class BetweenExpression(
    val value: String,
    val from: Any,
    val to: Any
) : Expression {
    override fun parse(): String = "$value >= $from && $value <= $to"
}
