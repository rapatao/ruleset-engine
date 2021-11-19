package com.rapatao.projects.ruleset.engine.expressions

import com.rapatao.projects.ruleset.engine.expressions.types.IsBetween
import com.rapatao.projects.ruleset.engine.expressions.types.IsEqualTo
import com.rapatao.projects.ruleset.engine.expressions.types.IsTrue

object ExpressionBuilder {

    fun field(field: String) = Builder(field)

    class Builder(private val field: String) {
        infix fun equalsTo(value: Any) = IsEqualTo(field, value)

        infix fun between(value: Any) = BetweenBuilder(value)

        fun isTrue() = IsTrue(field)

        fun isFalse() = IsTrue("!$field")

        inner class BetweenBuilder(private val value: Any) {
            infix fun and(to: Any) = IsBetween(field, value, to)
        }
    }
}
