package com.rapatao.projects.ruleset.engine.expressions.types

import com.rapatao.projects.ruleset.engine.expressions.Expression

data class IsBetween(
    val field: String,
    val from: Any,
    val to: Any
) : Expression {
    override fun parse(): String = "$field >= $from && $field <= $to"
}
