package com.rapatao.projects.ruleset.engine.expressions

open class JsExpression(private val expression: String?) : Expression {
    override fun parse(): String = "true == ($expression)"
}
