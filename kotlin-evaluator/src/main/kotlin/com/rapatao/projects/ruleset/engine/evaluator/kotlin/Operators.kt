package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.types.operators.ContainsOperator
import com.rapatao.projects.ruleset.engine.types.operators.EndsWithOperator
import com.rapatao.projects.ruleset.engine.types.operators.EqualsOperator
import com.rapatao.projects.ruleset.engine.types.operators.GreaterOrEqualThanOperator
import com.rapatao.projects.ruleset.engine.types.operators.GreaterThanOperator
import com.rapatao.projects.ruleset.engine.types.operators.LessOrEqualThanOperator
import com.rapatao.projects.ruleset.engine.types.operators.LessThanOperator
import com.rapatao.projects.ruleset.engine.types.operators.NotEqualsOperator
import com.rapatao.projects.ruleset.engine.types.operators.StartsWithOperator

internal class Equals : EqualsOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        left == right
}

internal class NotEquals : NotEqualsOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        left != right
}

internal class GreaterThan : GreaterThanOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        left.comparable() > right
}

internal class GreaterOrEqualThan : GreaterOrEqualThanOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        left.comparable() >= right
}

internal class LessThan : LessThanOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        left.comparable() < right
}

internal class LessOrEqualThan : LessOrEqualThanOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        left.comparable() <= right
}

internal class StartsWith : StartsWithOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        left.toString().startsWith(right.toString())
}

internal class EndsWith : EndsWithOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        left.toString().endsWith(right.toString())
}

internal class Contains : ContainsOperator() {
    override fun process(context: EvalContext, left: Any?, right: Any?): Boolean =
        left.checkContains(right)
}

@Suppress("UNCHECKED_CAST")
private fun <T> T.comparable() = this as Comparable<T>

private fun Any?.checkContains(value: Any?): Boolean {
    return when {
        this is String && value is String -> this.contains(value)
        this is Collection<*> -> this.contains(value)
        this is Array<*> -> this.contains(value)
        else -> throw UnsupportedOperationException("contains doesn't support ${this?.javaClass} type")
    }
}
