package com.rapatao.projects.ruleset.engine

data class RuleSet(
    val validations: List<Matcher>
) {
    companion object {
        fun validations(vararg matchers: Matcher) = RuleSet(validations = matchers.toList())
    }
}
