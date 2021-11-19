package com.rapatao.projects.ruleset.engine.expressions.types

import com.rapatao.projects.ruleset.engine.expressions.Expression

data class JsExpression(val expression: String) : Expression {
    override fun parse(): String = "true == ($expression)"
}
