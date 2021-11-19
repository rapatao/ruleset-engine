package com.rapatao.projects.ruleset.engine.expressions.types

import com.rapatao.projects.ruleset.engine.expressions.Expression

open class JsExpression(private val expression: String?) : Expression {
    override fun parse(): String = "true == ($expression)"
}
