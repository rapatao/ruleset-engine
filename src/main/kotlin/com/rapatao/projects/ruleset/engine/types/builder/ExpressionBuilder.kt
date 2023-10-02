package com.rapatao.projects.ruleset.engine.types.builder

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Expression.Operator.EQUALS
import com.rapatao.projects.ruleset.engine.types.Expression.Operator.GREATER_OR_EQUAL_THAN
import com.rapatao.projects.ruleset.engine.types.Expression.Operator.GREATER_THAN
import com.rapatao.projects.ruleset.engine.types.Expression.Operator.LESS_OR_EQUAL_THAN
import com.rapatao.projects.ruleset.engine.types.Expression.Operator.LESS_THAN

object ExpressionBuilder {

    fun left(left: Any) = Builder(left)

    fun isTrue(expression: Any) = left(expression).isTrue()
    fun isFalse(expression: Any) = left(expression).isFalse()

    class Builder(private val left: Any) {
        infix fun equalsTo(right: Any) = Expression(left, EQUALS, right)

        infix fun greaterThan(right: Any) = Expression(left, GREATER_THAN, right)

        infix fun greaterOrEqualThan(right: Any) = Expression(left, GREATER_OR_EQUAL_THAN, right)

        infix fun lessThan(right: Any) = Expression(left, LESS_THAN, right)

        infix fun lessOrEqualThan(right: Any) = Expression(left, LESS_OR_EQUAL_THAN, right)

        fun isTrue() = Expression(left, EQUALS, true)

        fun isFalse() = Expression(left, EQUALS, false)
    }
}
