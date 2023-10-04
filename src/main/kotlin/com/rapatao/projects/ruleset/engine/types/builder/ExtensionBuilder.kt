package com.rapatao.projects.ruleset.engine.types.builder

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.isFalse
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.isTrue
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.left

fun Any.asExpression(): Expression = this.takeIf { it !is Expression }?.let { isTrue(it) } ?: this as Expression

fun Any.isTrue(): Expression = isTrue(this)

fun Any.isFalse(): Expression = isFalse(this)

infix fun Any.equalsTo(right: Any): Expression = left(this).equalsTo(right)

infix fun Any.notEqualsTo(right: Any): Expression = left(this).notEqualsTo(right)

infix fun Any.greaterThan(from: Any): Expression = left(this).greaterThan(from)

infix fun Any.greaterOrEqualThan(from: Any): Expression = left(this).greaterOrEqualThan(from)

infix fun Any.lessThan(from: Any): Expression = left(this).lessThan(from)

infix fun Any.lessOrEqualThan(from: Any): Expression = left(this).lessOrEqualThan(from)

infix fun Expression.ifFail(use: OnFailure): Expression = Expression(
    left = this.left, operator = this.operator, right = this.right, onFailure = use,
)
