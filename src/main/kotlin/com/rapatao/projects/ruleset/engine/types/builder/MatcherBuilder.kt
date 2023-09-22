package com.rapatao.projects.ruleset.engine.types.builder

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Matcher
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.isTrue

object MatcherBuilder {
    fun expression(expression: Expression) = Matcher(expression = expression)
    fun expression(expression: Any) = Matcher(expression = isTrue(expression))
    fun allMatch(vararg matchers: Matcher) = Matcher(allMatch = matchers.toList())
    fun anyMatch(vararg matchers: Matcher) = Matcher(anyMatch = matchers.toList())
    fun noneMatch(vararg matchers: Matcher) = Matcher(noneMatch = matchers.toList())
}
