package com.rapatao.projects.ruleset

import com.rapatao.projects.ruleset.engine.Matcher
import org.mozilla.javascript.Context
import org.mozilla.javascript.ScriptableObject

class Evaluator {

    private val argumentPrefix = "$"

    fun evaluate(rule: Matcher, inputData: Any): Boolean {
        val begin = System.currentTimeMillis()

        val result = internalEvaluate(rule, inputData)

        val end = System.currentTimeMillis()

        println("RuleSet processed in ${end - begin} ms")

        return result
    }

    private fun internalEvaluate(rule: Matcher, inputData: Any): Boolean {
        val context = Context.enter()
        context.optimizationLevel = -1

        val scope = context.initSafeStandardObjects()

        val map = inputData.javaClass.declaredFields.associate {
            it.isAccessible = true
            Pair(it.name, it.get(inputData))
        }

        val params = map.createParams()

        params.forEach {
            val value = map[it]
            val jsObject = Context.javaToJS(value, scope)
            ScriptableObject.putConstProperty(scope, "$argumentPrefix$it", jsObject)
        }

        val processIsTrue = rule.expression?.processIsTrue(context, scope, params) ?: true
        val processNoneMatch = rule.noneMatch?.processNoneMatch(context, scope, params) ?: true
        val processAnyMatch = rule.anyMatch?.processAnyMatch(context, scope, params) ?: true
        val processAllMatch = rule.allMatch?.processAllMatch(context, scope, params) ?: true

        return processIsTrue && processNoneMatch && processAnyMatch && processAllMatch
    }

    private fun List<Matcher>.processNoneMatch(
        context: Context,
        scope: ScriptableObject,
        params: List<String>
    ): Boolean {
        val noneMatch = this.firstOrNull {
            val isTrue = it.expression?.processIsTrue(context, scope, params) == true
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
            val isTrue = it.expression?.processIsTrue(context, scope, params) == true
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
            val isTrue = it.expression?.processIsTrue(context, scope, params)?.isFalse() ?: false
            val anyMatch = it.anyMatch?.processAnyMatch(context, scope, params)?.isFalse() ?: false
            val noneMatch = it.noneMatch?.processNoneMatch(context, scope, params)?.isFalse() ?: false
            val allMatch = it.allMatch?.processAllMatch(context, scope, params)?.isFalse() ?: false

            isTrue || anyMatch || noneMatch || allMatch
        } == null

        return allMatch
    }

    private fun String.processIsTrue(context: Context, scope: ScriptableObject, params: List<String>): Boolean {
        val result = context.evaluateString(scope, this.buildScript(params), this, 1, null)

        println("$this == $result")

        return true == result
    }

    private fun String.buildScript(params: List<String>): String {
        val jsParams = params.joinToString(",") { param -> "$argumentPrefix$param" }
        return "(function($jsParams){return true == ($this)})($jsParams);"
    }

    private fun Map<String, Any>.createParams() = this.keys.map { it }

    private fun Boolean.isFalse(): Boolean = !this
}
