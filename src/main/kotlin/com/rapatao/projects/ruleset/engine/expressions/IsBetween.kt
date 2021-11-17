package com.rapatao.projects.ruleset.engine.expressions

class IsBetween(
    private val field: String,
    private val from: Any,
    private val to: Any
) : Expression {

    companion object {
        fun isFieldBetween(field: String) = IsBetweenInitialBuilder(field)
    }

    class IsBetweenInitialBuilder(private val field: String) {
        infix fun to(from: Any) = IsBetweenAndBuilder(field, from)
    }

    class IsBetweenAndBuilder(private val field: String, private val from: Any) {
        infix fun and(to: Any) = IsBetween(field, from, to)
    }

    override fun parse(): String = "$field >= $from && $field <= $to"
}
