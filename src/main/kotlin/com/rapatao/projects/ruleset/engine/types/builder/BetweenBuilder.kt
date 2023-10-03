package com.rapatao.projects.ruleset.engine.types.builder

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Operator

infix fun Any.from(from: Any): BetweenBuilder = BetweenBuilder(
    left = this, from = from, operator = Operator.GREATER_THAN,
)

infix fun Any.fromInclusive(from: Any): BetweenBuilder = BetweenBuilder(
    left = this, from = from, operator = Operator.GREATER_OR_EQUAL_THAN,
)

data class BetweenBuilder(val left: Any, val from: Any, val operator: Operator) {
    infix fun to(to: Any): Expression = MatcherBuilder.allMatch(
        ExpressionBuilder.expression(left = left, operator = operator, right = from),
        ExpressionBuilder.expression(left = left, operator = Operator.LESS_THAN, right = to),
    )

    infix fun toInclusive(to: Any): Expression = MatcherBuilder.allMatch(
        ExpressionBuilder.expression(left = left, operator = operator, right = from),
        ExpressionBuilder.expression(left = left, operator = Operator.LESS_OR_EQUAL_THAN, right = to),
    )
}

