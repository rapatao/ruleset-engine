package com.rapatao.projects.ruleset.engine

import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.Matcher
import org.mozilla.javascript.Context
import org.mozilla.javascript.ScriptableObject

class Evaluator {
    private val factory: InternalContextFactory = InternalContextFactory()

    fun evaluate(rule: Matcher, inputData: Any): Boolean {
        return internalEvaluate(rule, inputData)
    }

    private fun internalEvaluate(rule: Matcher, inputData: Any): Boolean {
        return factory.call(inputData) { context, scope, params ->
            val processIsTrue = rule.expression?.processExpression(context, scope, params) ?: true
            val processNoneMatch = rule.noneMatch?.processNoneMatch(context, scope, params) ?: true
            val processAnyMatch = rule.anyMatch?.processAnyMatch(context, scope, params) ?: true
            val processAllMatch = rule.allMatch?.processAllMatch(context, scope, params) ?: true

            processIsTrue && processNoneMatch && processAnyMatch && processAllMatch
        }
    }

    private fun List<Matcher>.processNoneMatch(
        context: Context,
        scope: ScriptableObject,
        params: List<String>
    ): Boolean {
        val noneMatch = this.firstOrNull {
            val isTrue = it.expression?.processExpression(context, scope, params) == true
            val noneMatch = it.noneMatch?.processNoneMatch(context, scope, params) == false
            val anyMatch = it.anyMatch?.processAnyMatch(context, scope, params) == true
            val allMatch = it.allMatch?.processAllMatch(context, scope, params) == true

            isTrue || noneMatch || anyMatch || allMatch
        } == null

        return noneMatch
    }

    private fun List<Matcher>.processAnyMatch(
        context: Context,
        scope: ScriptableObject,
        params: List<String>
    ): Boolean {
        val anyMatch = this.firstOrNull {
            val isTrue = it.expression?.processExpression(context, scope, params) == true
            val anyMatch = it.anyMatch?.processAnyMatch(context, scope, params) ?: false
            val noneMatch = it.noneMatch?.processNoneMatch(context, scope, params) == false
            val allMatch = it.allMatch?.processAllMatch(context, scope, params) == true

            isTrue || anyMatch || noneMatch || allMatch
        } != null

        return anyMatch
    }

    private fun List<Matcher>.processAllMatch(
        context: Context,
        scope: ScriptableObject,
        params: List<String>
    ): Boolean {
        val allMatch = this.firstOrNull {
            val isTrue = it.expression?.processExpression(context, scope, params) == false
            val anyMatch = it.anyMatch?.processAnyMatch(context, scope, params) == false
            val noneMatch = it.noneMatch?.processNoneMatch(context, scope, params) == false
            val allMatch = it.allMatch?.processAllMatch(context, scope, params) == false

            isTrue || anyMatch || noneMatch || allMatch
        } == null

        return allMatch
    }

    private fun Expression.processExpression(
        context: Context,
        scope: ScriptableObject,
        params: List<String>
    ): Boolean {
        val script = this.parse()
        val result = context.evaluateString(scope, script.buildScript(params), script, 1, null)

        return true == result
    }

    private fun String.buildScript(params: List<String>): String {
        val jsParams = params.joinToString(",")
        return "(function($jsParams){return true == ($this)})($jsParams);"
    }
}
