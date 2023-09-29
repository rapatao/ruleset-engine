package com.rapatao.projects.ruleset.engine.context

import com.rapatao.projects.ruleset.engine.parameters.MapInjector
import com.rapatao.projects.ruleset.engine.parameters.TypedInjector
import org.mozilla.javascript.Context
import org.mozilla.javascript.ContextFactory
import org.mozilla.javascript.ScriptableObject

open class ContextFactory(
    val optimizationLevel: Int = -1,
    val wrapJavaPrimitives: Boolean = false,
) : ContextFactory() {
    override fun hasFeature(cx: Context, featureIndex: Int): Boolean {
        if (Context.FEATURE_ENABLE_JAVA_MAP_ACCESS == featureIndex) {
            return true
        }

        return super.hasFeature(cx, featureIndex)
    }

    fun call(
        inputData: Any,
        block: (context: Context, scope: ScriptableObject) -> Boolean
    ): Boolean {
        return this.call { context ->
            context.optimizationLevel = optimizationLevel
            context.wrapFactory.isJavaPrimitiveWrap = wrapJavaPrimitives

            val scope = context.initSafeStandardObjects()

            parseParameters(scope, context, inputData)

            block(context, scope)
        }
    }

    private fun parseParameters(
        scope: ScriptableObject,
        context: Context,
        inputData: Any,
    ) {
        when (inputData) {
            is Map<*, *> -> MapInjector.inject(scope, context, inputData)
            else -> TypedInjector.inject(scope, context, inputData)
        }
    }
}
