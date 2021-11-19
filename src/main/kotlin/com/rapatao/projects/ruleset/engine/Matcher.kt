package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.expressions.Expression
import com.rapatao.projects.ruleset.engine.expressions.types.IsTrue

data class Matcher(
    val allMatch: List<Matcher>? = null,
    val anyMatch: List<Matcher>? = null,
    val noneMatch: List<Matcher>? = null,
    val expression: Expression? = null
) {
    companion object {
        fun expression(expression: Expression) = Matcher(expression = expression)
        fun expression(expression: String) = Matcher(expression = IsTrue(expression))
        fun allMatch(vararg matchers: Matcher) = Matcher(allMatch = matchers.toList())
        fun anyMatch(vararg matchers: Matcher) = Matcher(anyMatch = matchers.toList())
        fun noneMatch(vararg matchers: Matcher) = Matcher(noneMatch = matchers.toList())
    }
}
