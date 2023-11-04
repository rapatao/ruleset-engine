package com.rapatao.projects.ruleset.engine.types.builder

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Operator

/**
 * Creates a [BetweenBuilder] object to build a between condition.
 *
 * @param from The lower boundary value.
 * @return A [BetweenBuilder] object to build the between condition.
 */
infix fun Any.from(from: Any): BetweenBuilder = BetweenBuilder(
    left = this, from = from, operator = Operator.GREATER_THAN,
)

/**
 * Creates a [BetweenBuilder] with the given left operand, starting value, and comparison operator
 *
 * @param from The starting value of the range, inclusive
 * @return The [BetweenBuilder] instance for further chaining
 */
infix fun Any.fromInclusive(from: Any): BetweenBuilder = BetweenBuilder(
    left = this, from = from, operator = Operator.GREATER_OR_EQUAL_THAN,
)

/**
 * A builder class for constructing expressions representing a between condition.
 *
 * @property left The left operand of the condition.
 * @property from The starting value of the range.
 * @property operator The comparison operator for the condition.
 */
data class BetweenBuilder(val left: Any, val from: Any, val operator: Operator) {
    /**
     * Creates a non-inclusive range expression using the given `to` value.
     *
     * @param to The upper bound value to compare against.
     * @return A range expression.
     */
    infix fun to(to: Any): Expression = MatcherBuilder.allMatch(
        ExpressionBuilder.expression(left = left, operator = operator, right = from),
        ExpressionBuilder.expression(left = left, operator = Operator.LESS_THAN, right = to),
    )

    /**
     * Creates an inclusive range expression using the given `to` value.
     *
     * @param to The upper bound of the range.
     * @return A range expression
     */
    infix fun toInclusive(to: Any): Expression = MatcherBuilder.allMatch(
        ExpressionBuilder.expression(left = left, operator = operator, right = from),
        ExpressionBuilder.expression(left = left, operator = Operator.LESS_OR_EQUAL_THAN, right = to),
    )
}

