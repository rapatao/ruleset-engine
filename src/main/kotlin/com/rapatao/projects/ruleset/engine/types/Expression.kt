package com.rapatao.projects.ruleset.engine.types

interface Expression {
    fun parse(): String

    fun wrap(value: Any): Any {
        if (value is String && value.contains(" ")) {
            return "\"$value\""
        }
        return value
    }
}
