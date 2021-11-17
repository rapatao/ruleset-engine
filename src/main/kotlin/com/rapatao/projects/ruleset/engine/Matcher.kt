package com.rapatao.projects.ruleset.engine

data class Matcher(
    val allMatch: List<Matcher>? = null,
    val anyMatch: List<Matcher>? = null,
    val noneMatch: List<Matcher>? = null,
    val expression: String? = null
) {
    companion object {
        fun expression(rule: String) = Matcher(expression = rule)
        fun allMatch(vararg matchers: Matcher) = Matcher(allMatch = matchers.toList())
        fun anyMatch(vararg matchers: Matcher) = Matcher(anyMatch = matchers.toList())
        fun noneMatch(vararg matchers: Matcher) = Matcher(noneMatch = matchers.toList())
    }
}
