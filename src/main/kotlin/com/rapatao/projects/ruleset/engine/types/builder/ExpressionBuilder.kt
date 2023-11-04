package com.rapatao.projects.ruleset.engine.types.builder

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Operator
import com.rapatao.projects.ruleset.engine.types.Operator.EQUALS
import com.rapatao.projects.ruleset.engine.types.Operator.GREATER_OR_EQUAL_THAN
import com.rapatao.projects.ruleset.engine.types.Operator.GREATER_THAN
import com.rapatao.projects.ruleset.engine.types.Operator.LESS_OR_EQUAL_THAN
import com.rapatao.projects.ruleset.engine.types.Operator.LESS_THAN
import com.rapatao.projects.ruleset.engine.types.Operator.NOT_EQUALS

/**
 * A utility class for building expressions using different operators.
 */
object ExpressionBuilder {

    /**
     * Creates a new instance of [Builder] with the given [left] parameter.
     *
     * @param left the value to set as the left property of the [Builder] instance.
     */
    fun left(left: Any) = Builder(left)

    /**
     * Checks if the given expression evaluates to true.
     *
     * @param expression The expression to be evaluated.
     * @return true if the expression evaluates to true, false otherwise.
     */
    fun isTrue(expression: Any) = left(expression).isTrue()

    /**
     * Checks if the given expression evaluates to false.
     *
     * @param expression the expression to be evaluated
     * @return true if the expression evaluates to false, false otherwise
     */
    fun isFalse(expression: Any) = left(expression).isFalse()

    /**
     * Creates an expression using the given left operand, operator, and right operand.
     *
     * @param left The left operand of the expression.
     * @param operator The operator of the expression.
     * @param right The right operand of the expression.
     * @return The created expression.
     */
    fun expression(left: Any, operator: Operator, right: Any) = Expression(
        left = left, operator = operator, right = right
    )

    /**
     * Represents a builder for creating expressions.
     *
     * @param left The left-hand side of the expression.
     */
    class Builder(private val left: Any) {
        /**
         * Creates an expression that represents the "equals to" comparison between the left operand and the right
         * operand.
         *
         * @param right The right operand to compare.
         * @return The expression representing the "equals to" comparison.
         */
        infix fun equalsTo(right: Any) = Expression(left = left, operator = EQUALS, right = right)

        /**
         * Creates an expression representing the inequality comparison between the left-hand side and the right-hand
         * side.
         *
         * @param right The right-hand side of the comparison.
         * @return A new expression representing the inequality comparison.
         */
        infix fun notEqualsTo(right: Any) = Expression(left = left, operator = NOT_EQUALS, right = right)

        /**
         * Creates an Expression object representing the greater-than comparison between the left operand and the right
         * operand.
         *
         * @param right the right operand to compare with the left operand
         *
         * @return the Expression object representing the greater-than comparison
         */
        infix fun greaterThan(right: Any) =
            Expression(left = left, operator = GREATER_THAN, right = right)

        /**
         * Creates an expression that represents the "greater than or equal to" operation
         *
         * @param right the value to compare with
         * @return an expression representing the "greater than or equal to" operation
         */
        infix fun greaterOrEqualThan(right: Any) =
            Expression(left = left, operator = GREATER_OR_EQUAL_THAN, right = right)

        /**
         * Creates an 'less than' expression with the specified 'right' value.
         *
         * @param right The right value of the expression.
         * @return The created expression.
         */
        infix fun lessThan(right: Any) = Expression(left = left, operator = LESS_THAN, right = right)

        /**
         * Creates an expression that represents the less than or equal to operation.
         *
         * @param right The value to compare against the left operand.
         * @return An Expression object representing the less than or equal to operation.
         */
        infix fun lessOrEqualThan(right: Any) =
            Expression(left = left, operator = LESS_OR_EQUAL_THAN, right = right)

        /**
         * Checks if the left operand represents a boolean with value true.
         *
         * @return The boolean expression representing if the condition is true.
         */
        fun isTrue() = Expression(left = left, operator = EQUALS, right = true)

        /**
         * Checks if the left operand represents a boolean with value false.
         *
         * @return The boolean expression representing if the condition is false.
         */
        fun isFalse() = Expression(left = left, operator = EQUALS, right = false)
    }
}
