package com.rapatao.projects.ruleset.engine.expressions.types

import com.rapatao.projects.ruleset.engine.expressions.Expression

class IsEqualTo(private val field: String, private val value: Any) : Expression {
    override fun parse(): String = "$field == $value"
}
