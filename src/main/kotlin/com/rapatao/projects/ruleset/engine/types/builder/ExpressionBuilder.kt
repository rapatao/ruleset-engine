package com.rapatao.projects.ruleset.engine.types.builder

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Operator
import com.rapatao.projects.ruleset.engine.types.Operator.EQUALS
import com.rapatao.projects.ruleset.engine.types.Operator.GREATER_OR_EQUAL_THAN
import com.rapatao.projects.ruleset.engine.types.Operator.GREATER_THAN
import com.rapatao.projects.ruleset.engine.types.Operator.LESS_OR_EQUAL_THAN
import com.rapatao.projects.ruleset.engine.types.Operator.LESS_THAN

object ExpressionBuilder {

    fun left(left: Any) = Builder(left)

    fun isTrue(expression: Any) = left(expression).isTrue()
    fun isFalse(expression: Any) = left(expression).isFalse()

    fun expression(left: Any, operator: Operator, right: Any) = Expression(
        left = left, operator = operator, right = right
    )

    class Builder(private val left: Any) {
        infix fun equalsTo(right: Any) = Expression(left = left, operator = EQUALS, right = right)

        infix fun greaterThan(right: Any) =
            Expression(left = left, operator = GREATER_THAN, right = right)

        infix fun greaterOrEqualThan(right: Any) =
            Expression(left = left, operator = GREATER_OR_EQUAL_THAN, right = right)

        infix fun lessThan(right: Any) = Expression(left = left, operator = LESS_THAN, right = right)

        infix fun lessOrEqualThan(right: Any) =
            Expression(left = left, operator = LESS_OR_EQUAL_THAN, right = right)

        fun isTrue() = Expression(left = left, operator = EQUALS, right = true)

        fun isFalse() = Expression(left = left, operator = EQUALS, right = false)
    }
}
