package com.rapatao.projects.ruleset.engine.context

import com.rapatao.projects.ruleset.engine.parameters.MapInjector
import com.rapatao.projects.ruleset.engine.parameters.TypedInjector
import org.mozilla.javascript.Context
import org.mozilla.javascript.ContextFactory
import org.mozilla.javascript.ScriptableObject

/**
 * A factory for creating JavaScript contexts with customizable options.
 *
 * @property optimizationLevel The optimization level for the context. Defaults to -1.
 * @property wrapJavaPrimitives Determines if Java primitive values should be wrapped in their corresponding JavaScript
 * wrapper objects. Defaults to false.
 * @property languageVersion The language version of the JavaScript code to be executed in the context.
 * Defaults to Context.VERSION_DEFAULT.
 */
open class ContextFactory(
    val optimizationLevel: Int = -1,
    val wrapJavaPrimitives: Boolean = false,
    val languageVersion: Int = Context.VERSION_DEFAULT
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

    /**
     * Executes the provided block of code with the given input data and returns a boolean value indicating
     * the success or failure of the execution.
     *
     * @param inputData The input data to be used in the execution.
     * @param block A lambda function that takes in a context and a scope as parameters and returns a boolean value.
     *              The context represents the context in which the execution takes place, and the scope represents
     *              the scope of the execution.
     * @return The result of the execution.
     */
    open fun <T> call(
        inputData: Any,
        block: (context: Context, scope: ScriptableObject) -> T
    ): T {
        return this.call { context ->
            val scope = context.initSafeStandardObjects()

            parseParameters(scope, context, inputData)

            block(context, scope)
        }
    }

    /**
     * Parses parameters and injects them into the given scope based on the input data.
     *
     * @param scope the scriptable object scope where the parameters will be injected
     * @param context the context in which the parameters will be injected
     * @param inputData the input data containing the parameters
     */
    open fun parseParameters(
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
