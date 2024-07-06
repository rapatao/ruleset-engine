package com.rapatao.projects.ruleset.engine.types

import com.rapatao.projects.ruleset.engine.Evaluator
import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.Operator
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ExpressionTest {

    private val dummyEval: Evaluator = object : Evaluator(
        listOf(
            object : Operator {
                override fun process(context: EvalContext, left: Any?, right: Any?): Boolean = true
                override fun name(): String = "equals"
            }
        )
    ) {
        override fun <T> call(inputData: Any, block: (context: EvalContext) -> T): T {
            TODO("Not yet implemented")
        }

        override fun name(): String = "test"
    }

    @Test
    @DisplayName("empty expression must be invalid")
    fun assertEmptyExpression() {
        val exp = Expression()
        assertThat(exp.isValid(dummyEval), equalTo(false))
    }

    @Test
    @DisplayName("expression with operator is valid")
    fun assertExpressionValid() {
        val exp = Expression(
            operator = "equals",
        )

        assertThat(exp.isValid(dummyEval), equalTo(true))
    }

    @Test
    @DisplayName("no operator, but with noneMatch is valid")
    fun assertNoOperatorByNoneMatch() {
        val exp = Expression(
            noneMatch = listOf(Expression(operator = "equals")),
        )

        assertThat(exp.isValid(dummyEval), equalTo(true))
    }

    @Test
    @DisplayName("no operator, but with anyMatch is valid")
    fun assertNoOperatorButWithAnyMatch() {
        val exp = Expression(
            anyMatch = listOf(Expression(operator = "equals")),
        )

        assertThat(exp.isValid(dummyEval), equalTo(true))
    }

    @Test
    @DisplayName("no operator, but with allMatch is valid")
    fun assertNoOperatorButWithAllMatch() {
        val exp = Expression(
            allMatch = listOf(Expression(operator = "equals")),
        )

        assertThat(exp.isValid(dummyEval), equalTo(true))
    }

    @Test
    @DisplayName("is valid if group and operator is set and valid")
    fun assertIsValidIfGroupAndOperatorIsSetAndValid() {
        val exp = Expression(
            allMatch = listOf(Expression(operator = "equals")),
            noneMatch = listOf(Expression(operator = "equals")),
            anyMatch = listOf(Expression(operator = "equals")),
        )

        assertThat(exp.isValid(dummyEval), equalTo(true))
    }

    @Test
    @DisplayName("is invalid if allMatch contains an invalid expression")
    fun assertIsInvalidIfAllMatchContainsAnInvalidExpression() {
        val exp = Expression(
            allMatch = listOf(Expression()),
            noneMatch = listOf(Expression(operator = "equals")),
            anyMatch = listOf(Expression(operator = "equals")),
        )

        assertThat(exp.isValid(dummyEval), equalTo(false))
    }

    @Test
    @DisplayName("is invalid if noneMatch contains an invalid expression")
    fun assertIsInvalidIfNoneMatchContainsInvalidExpression() {
        val exp = Expression(
            allMatch = listOf(Expression(operator = "equals")),
            noneMatch = listOf(Expression()),
            anyMatch = listOf(Expression(operator = "equals")),
        )

        assertThat(exp.isValid(dummyEval), equalTo(false))
    }

    @Test
    @DisplayName("is invalid if anyMatch contains an invalid expression")
    fun assertIsInvalidIfAnyMatchContainsInvalidExpression() {
        val exp = Expression(
            allMatch = listOf(Expression(operator = "equals")),
            noneMatch = listOf(Expression(operator = "equals")),
            anyMatch = listOf(Expression()),
        )

        assertThat(exp.isValid(dummyEval), equalTo(false))
    }
}
