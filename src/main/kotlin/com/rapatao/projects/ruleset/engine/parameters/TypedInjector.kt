package com.rapatao.projects.ruleset.engine.parameters

import org.mozilla.javascript.Context
import org.mozilla.javascript.ScriptableObject
import kotlin.reflect.full.memberProperties

internal data object TypedInjector : ParameterInjector<Any> {
    override fun inject(scope: ScriptableObject, context: Context, input: Any) {
        input.javaClass.kotlin.memberProperties.forEach {
            set(scope, context, it.name, it.get(input))
        }
    }
}
