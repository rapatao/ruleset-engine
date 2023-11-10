package com.rapatao.projects.ruleset.engine.rhino.parameters

import org.mozilla.javascript.Context
import org.mozilla.javascript.ScriptableObject

internal data object MapInjector : ParameterInjector<Map<*, *>> {
    override fun inject(scope: ScriptableObject, context: Context, input: Map<*, *>) {
        input.forEach {
            set(scope, context, it.key.toString(), it.value)
        }
    }
}
