package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.context.ContextFactory
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Matcher
import org.mozilla.javascript.Context
import org.mozilla.javascript.Script
import org.mozilla.javascript.ScriptableObject

class Evaluator(
    val contextFactory: ContextFactory = ContextFactory(),
) {

    fun evaluate(rule: Matcher, inputData: Any): Boolean {
        return contextFactory.call(inputData) { context, scope ->
            val processIsTrue = rule.expression?.processExpression(context, scope) ?: true
            val processNoneMatch = rule.noneMatch?.processNoneMatch(context, scope) ?: true
            val processAnyMatch = rule.anyMatch?.processAnyMatch(context, scope) ?: true
            val processAllMatch = rule.allMatch?.processAllMatch(context, scope) ?: true

            processIsTrue && processNoneMatch && processAnyMatch && processAllMatch
        }
    }

    private fun List<Matcher>.processNoneMatch(
        context: Context,
        scope: ScriptableObject,
    ): Boolean {
        val noneMatch = this.firstOrNull {
            val isTrue = it.expression?.processExpression(context, scope) == true
            val noneMatch = it.noneMatch?.processNoneMatch(context, scope) == false
            val anyMatch = it.anyMatch?.processAnyMatch(context, scope) == true
            val allMatch = it.allMatch?.processAllMatch(context, scope) == true

            isTrue || noneMatch || anyMatch || allMatch
        } == null

        return noneMatch
    }

    private fun List<Matcher>.processAnyMatch(
        context: Context,
        scope: ScriptableObject,
    ): Boolean {
        val anyMatch = this.firstOrNull {
            val isTrue = it.expression?.processExpression(context, scope) == true
            val anyMatch = it.anyMatch?.processAnyMatch(context, scope) ?: false
            val noneMatch = it.noneMatch?.processNoneMatch(context, scope) == false
            val allMatch = it.allMatch?.processAllMatch(context, scope) == true

            isTrue || anyMatch || noneMatch || allMatch
        } != null

        return anyMatch
    }

    private fun List<Matcher>.processAllMatch(
        context: Context,
        scope: ScriptableObject,
    ): Boolean {
        val allMatch = this.firstOrNull {
            val isTrue = it.expression?.processExpression(context, scope) == false
            val anyMatch = it.anyMatch?.processAnyMatch(context, scope) == false
            val noneMatch = it.noneMatch?.processNoneMatch(context, scope) == false
            val allMatch = it.allMatch?.processAllMatch(context, scope) == false

            isTrue || anyMatch || noneMatch || allMatch
        } == null

        return allMatch
    }

    private fun Expression.processExpression(
        context: Context,
        scope: ScriptableObject,
    ): Boolean {
        return true == this.parse()
            .asScript(context)
            .exec(context, scope)
    }

    private fun String.asScript(context: Context): Script {
        return context.compileString(
            "true == ($this)",
            this,
            0,
            null,
        )
    }
}
