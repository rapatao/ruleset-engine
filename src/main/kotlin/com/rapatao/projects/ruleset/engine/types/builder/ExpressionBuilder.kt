package com.rapatao.projects.ruleset.engine.types.builder

import com.rapatao.projects.ruleset.engine.types.BetweenExpression
import com.rapatao.projects.ruleset.engine.types.BooleanExpression
import com.rapatao.projects.ruleset.engine.types.BooleanExpression.Operator.EQUALS
import com.rapatao.projects.ruleset.engine.types.BooleanExpression.Operator.GREATER_OR_EQUAL_THAN
import com.rapatao.projects.ruleset.engine.types.BooleanExpression.Operator.GREATER_THAN
import com.rapatao.projects.ruleset.engine.types.BooleanExpression.Operator.LESS_OR_EQUAL_THAN
import com.rapatao.projects.ruleset.engine.types.BooleanExpression.Operator.LESS_THAN

object ExpressionBuilder {

    fun field(field: String) = Builder(field)

    fun isTrue(expression: String) = BooleanExpression(expression, EQUALS, true)

    class Builder(private val field: String) {
        infix fun equalsTo(value: Any) = BooleanExpression(field, EQUALS, value)

        infix fun between(value: Any) = BetweenBuilder(value)

        infix fun greaterThan(value: Any) = BooleanExpression(field, GREATER_THAN, value)

        infix fun greaterOrEqualThan(value: Any) = BooleanExpression(field, GREATER_OR_EQUAL_THAN, value)

        infix fun lessThan(value: Any) = BooleanExpression(field, LESS_THAN, value)

        infix fun lessOrEqualThan(value: Any) = BooleanExpression(field, LESS_OR_EQUAL_THAN, value)

        fun isTrue() = BooleanExpression(field, EQUALS, true)

        fun isFalse() = BooleanExpression(field, EQUALS, false)

        inner class BetweenBuilder(private val value: Any) {
            infix fun and(to: Any) = BetweenExpression(field, value, to)
        }
    }
}
