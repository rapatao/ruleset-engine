package com.rapatao.projects.ruleset.engine.expressions.types

import com.rapatao.projects.ruleset.engine.expressions.Expression

class IsBetween(
    private val field: String,
    private val from: Any,
    private val to: Any
) : Expression {
    override fun parse(): String = "$field >= $from && $field <= $to"
}
