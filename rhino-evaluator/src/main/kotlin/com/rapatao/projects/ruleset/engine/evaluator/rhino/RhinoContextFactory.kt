package com.rapatao.projects.ruleset.engine.evaluator.rhino

import org.mozilla.javascript.Context
import org.mozilla.javascript.ContextFactory

/**
 * @property optimizationLevel The optimization level for the context. Defaults to -1.
 * @property wrapJavaPrimitives Determines if Java primitive values should be wrapped in their corresponding JavaScript
 * wrapper objects. Defaults to false.
 * @property languageVersion The language version of the JavaScript code to be executed in the context.
 * Defaults to Context.VERSION_DEFAULT.
 *
 * @see org.mozilla.javascript.Context
 */
open class RhinoContextFactory(
    private val optimizationLevel: Int = -1,
    private val wrapJavaPrimitives: Boolean = false,
    private val languageVersion: Int = Context.VERSION_DEFAULT,
) : ContextFactory() {

    /**
     * Check if the given feature is enabled in the context.
     *
     * @param cx the context in which to check the feature
     * @param featureIndex the index of the feature to check
     * @return true if the feature is enabled, false otherwise
     */
    override fun hasFeature(cx: Context, featureIndex: Int): Boolean {
        if (Context.FEATURE_ENABLE_JAVA_MAP_ACCESS == featureIndex) {
            return true
        }

        return super.hasFeature(cx, featureIndex)
    }

    /**
     * Creates and configures a new context for executing JavaScript code.
     *
     * @return The newly created context.
     */
    override fun makeContext(): Context {
        val context = super.makeContext()

        context.optimizationLevel = optimizationLevel
        context.wrapFactory.isJavaPrimitiveWrap = wrapJavaPrimitives
        context.languageVersion = languageVersion

        return context
    }
}
