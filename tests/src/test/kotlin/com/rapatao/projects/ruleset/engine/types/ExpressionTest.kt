package com.rapatao.projects.ruleset.engine.types

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test

class ExpressionTest {

    @Test
    fun `empty expression must be invalid`() {
        val exp = Expression()
        assertThat(exp.isValid(), equalTo(false))
    }

    @Test
    fun `expression with operator is valid`() {
        val exp = Expression(
            operator = Operator.EQUALS,
        )

        assertThat(exp.isValid(), equalTo(true))
    }

    @Test
    fun `no operator, but with noneMatch is valid`() {
        val exp = Expression(
            noneMatch = listOf(Expression(operator = Operator.EQUALS)),
        )

        assertThat(exp.isValid(), equalTo(true))
    }

    @Test
    fun `no operator, but with anyMatch is valid`() {
        val exp = Expression(
            anyMatch = listOf(Expression(operator = Operator.EQUALS)),
        )

        assertThat(exp.isValid(), equalTo(true))
    }

    @Test
    fun `no operator, but with allMatch is valid`() {
        val exp = Expression(
            allMatch = listOf(Expression(operator = Operator.EQUALS)),
        )

        assertThat(exp.isValid(), equalTo(true))
    }

    @Test
    fun `is valid if group and operator is set and valid`() {
        val exp = Expression(
            allMatch = listOf(Expression(operator = Operator.EQUALS)),
            noneMatch = listOf(Expression(operator = Operator.EQUALS)),
            anyMatch = listOf(Expression(operator = Operator.EQUALS)),
        )

        assertThat(exp.isValid(), equalTo(true))
    }


    @Test
    fun `is invalid if allMatch contains an invalid expression`() {
        val exp = Expression(
            allMatch = listOf(Expression()),
            noneMatch = listOf(Expression(operator = Operator.EQUALS)),
            anyMatch = listOf(Expression(operator = Operator.EQUALS)),
        )

        assertThat(exp.isValid(), equalTo(false))
    }

    @Test
    fun `is invalid if noneMatch contains an invalid expression`() {
        val exp = Expression(
            allMatch = listOf(Expression(operator = Operator.EQUALS)),
            noneMatch = listOf(Expression()),
            anyMatch = listOf(Expression(operator = Operator.EQUALS)),
        )

        assertThat(exp.isValid(), equalTo(false))
    }

    @Test
    fun `is invalid if anyMatch contains an invalid expression`() {
        val exp = Expression(
            allMatch = listOf(Expression(operator = Operator.EQUALS)),
            noneMatch = listOf(Expression(operator = Operator.EQUALS)),
            anyMatch = listOf(Expression()),
        )

        assertThat(exp.isValid(), equalTo(false))
    }
}
