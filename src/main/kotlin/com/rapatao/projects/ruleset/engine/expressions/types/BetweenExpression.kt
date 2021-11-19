package com.rapatao.projects.ruleset.engine.expressions.types

import com.rapatao.projects.ruleset.engine.expressions.Expression

data class BetweenExpression(
    val value: String,
    val from: Any,
    val to: Any
) : Expression {
    override fun parse(): String = "$value >= $from && $value <= $to"
}
