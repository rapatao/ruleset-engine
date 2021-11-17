package com.rapatao.projects.ruleset.engine.expressions

class IsTrue(field: String) : JsExpression(field) {
    companion object {
        fun isTrue(field: String) = IsTrue(field)
    }
}
