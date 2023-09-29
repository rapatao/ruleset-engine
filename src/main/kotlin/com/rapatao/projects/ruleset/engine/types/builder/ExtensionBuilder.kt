package com.rapatao.projects.ruleset.engine.types.builder

import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.isFalse
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.isTrue
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.left

fun Any.asExpression() = isTrue(this)
fun Any.isTrue() = isTrue(this)
fun Any.isFalse() = isFalse(this)
infix fun Any.equalsTo(right: Any) = left(this).equalsTo(right)
infix fun Any.between(from: Any) = left(this).between(from)
infix fun Any.greaterThan(from: Any) = left(this).greaterThan(from)
infix fun Any.greaterOrEqualThan(from: Any) = left(this).greaterOrEqualThan(from)
infix fun Any.lessThan(from: Any) = left(this).lessThan(from)
infix fun Any.lessOrEqualThan(from: Any) = left(this).lessOrEqualThan(from)
