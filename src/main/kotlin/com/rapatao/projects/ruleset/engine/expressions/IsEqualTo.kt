package com.rapatao.projects.ruleset.engine.expressions

class IsEqualTo(private val field: String, private val value: Any) : Expression {

    companion object {
        fun isFieldEquals(field: String): IsEqualToBuilder = IsEqualToBuilder(field)
    }

    class IsEqualToBuilder(private val field: String) {
        infix fun to(value: Any) = IsEqualTo(field, value)
    }

    override fun parse(): String = "$field == $value"
}
