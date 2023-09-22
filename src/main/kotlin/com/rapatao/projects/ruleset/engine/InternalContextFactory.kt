package com.rapatao.projects.ruleset.engine

import org.mozilla.javascript.Context
import org.mozilla.javascript.ContextFactory
import org.mozilla.javascript.ScriptableObject
import kotlin.reflect.full.memberProperties

internal class InternalContextFactory : ContextFactory() {
    override fun hasFeature(cx: Context?, featureIndex: Int): Boolean {
        if (Context.FEATURE_ENABLE_JAVA_MAP_ACCESS == featureIndex) {
            return true
        }

        return super.hasFeature(cx, featureIndex)
    }

    fun call(
        inputData: Any,
        block: (context: Context, scope: ScriptableObject, params: List<String>) -> Boolean
    ): Boolean {
        return this.call { context ->
            context.optimizationLevel = -1

            val scope = context.initSafeStandardObjects()

            val params = parseParameters(inputData, scope, context)

            block(context, scope, params)
        }
    }

    private fun parseParameters(
        inputData: Any,
        scope: ScriptableObject,
        context: Context,
    ): List<String> {
        return when (inputData) {
            is Map<*, *> -> {
                inputData.onEach {
                    ScriptableObject.putConstProperty(
                        scope,
                        it.key.toString(),
                        Context.javaToJS(it.value, scope)
                    )
                }.map {
                    it.key.toString()
                }
            }

            else -> {
                inputData.javaClass.kotlin.memberProperties
                    .onEach {
                        ScriptableObject.putConstProperty(
                            scope,
                            it.name,
                            Context.javaToJS(it.get(inputData), scope, context)
                        )
                    }
                    .map { it.name }
            }
        }
    }
}
