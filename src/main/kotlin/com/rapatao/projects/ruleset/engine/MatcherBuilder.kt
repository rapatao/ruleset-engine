package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.expressions.Expression
import com.rapatao.projects.ruleset.engine.expressions.types.JsExpression

object MatcherBuilder {
    fun expression(expression: Expression) = Matcher(expression = expression)
    fun expression(expression: String) = Matcher(expression = JsExpression(expression))
    fun allMatch(vararg matchers: Matcher) = Matcher(allMatch = matchers.toList())
    fun anyMatch(vararg matchers: Matcher) = Matcher(anyMatch = matchers.toList())
    fun noneMatch(vararg matchers: Matcher) = Matcher(noneMatch = matchers.toList())
}
