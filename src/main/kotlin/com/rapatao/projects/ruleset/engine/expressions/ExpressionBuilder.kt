package com.rapatao.projects.ruleset.engine.expressions

import com.rapatao.projects.ruleset.engine.expressions.types.BooleanExpression
import com.rapatao.projects.ruleset.engine.expressions.types.BooleanExpression.Operator.EQUALS
import com.rapatao.projects.ruleset.engine.expressions.types.BooleanExpression.Operator.GREATER_OR_EQUAL_THAN
import com.rapatao.projects.ruleset.engine.expressions.types.BooleanExpression.Operator.GREATER_THAN
import com.rapatao.projects.ruleset.engine.expressions.types.BooleanExpression.Operator.LESS_THAN
import com.rapatao.projects.ruleset.engine.expressions.types.IsBetween
import com.rapatao.projects.ruleset.engine.expressions.types.JsExpression

object ExpressionBuilder {

    fun field(field: String) = Builder(field)

    class Builder(private val field: String) {
        infix fun equalsTo(value: Any) = BooleanExpression(field, EQUALS, value)

        infix fun between(value: Any) = BetweenBuilder(value)

        infix fun greaterThan(value: Any) = BooleanExpression(field, GREATER_THAN, value)

        infix fun greaterOrEqualThan(value: Any) = BooleanExpression(field, GREATER_OR_EQUAL_THAN, value)

        infix fun lessThan(value: Any) = BooleanExpression(field, LESS_THAN, value)

        infix fun lessOrEqualThan(value: Any) =
            BooleanExpression(field, BooleanExpression.Operator.LESS_OR_EQUAL_THAN, value)

        fun isTrue() = JsExpression(field)

        fun isFalse() = JsExpression("!$field")

        inner class BetweenBuilder(private val value: Any) {
            infix fun and(to: Any) = IsBetween(field, value, to)
        }
    }
}
