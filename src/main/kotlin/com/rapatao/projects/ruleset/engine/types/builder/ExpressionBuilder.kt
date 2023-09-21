package com.rapatao.projects.ruleset.engine.types.builder

import com.rapatao.projects.ruleset.engine.types.BetweenExpression
import com.rapatao.projects.ruleset.engine.types.BooleanExpression
import com.rapatao.projects.ruleset.engine.types.BooleanExpression.Operator.EQUALS
import com.rapatao.projects.ruleset.engine.types.BooleanExpression.Operator.GREATER_OR_EQUAL_THAN
import com.rapatao.projects.ruleset.engine.types.BooleanExpression.Operator.GREATER_THAN
import com.rapatao.projects.ruleset.engine.types.BooleanExpression.Operator.LESS_OR_EQUAL_THAN
import com.rapatao.projects.ruleset.engine.types.BooleanExpression.Operator.LESS_THAN

object ExpressionBuilder {

    @Deprecated("use ExpressionBuilder.left method",
        ReplaceWith("left(left)", "com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.left")
    )
    fun field(left: String) = left(left)

    fun left(left: Any) = Builder(left)

    fun isTrue(expression: Any) = BooleanExpression(expression, EQUALS, true)

    class Builder(private val left: Any) {
        infix fun equalsTo(right: Any) = BooleanExpression(left, EQUALS, right)

        infix fun between(from: Any) = BetweenBuilder(from)

        infix fun greaterThan(right: Any) = BooleanExpression(left, GREATER_THAN, right)

        infix fun greaterOrEqualThan(right: Any) = BooleanExpression(left, GREATER_OR_EQUAL_THAN, right)

        infix fun lessThan(right: Any) = BooleanExpression(left, LESS_THAN, right)

        infix fun lessOrEqualThan(right: Any) = BooleanExpression(left, LESS_OR_EQUAL_THAN, right)

        fun isTrue() = BooleanExpression(left, EQUALS, true)

        fun isFalse() = BooleanExpression(left, EQUALS, false)

        inner class BetweenBuilder(private val from: Any) {
            infix fun and(to: Any) = BetweenExpression(left, from, to)
        }
    }
}
