package com.rapatao.projects.ruleset.engine.evaluator.rhino.parameters

import org.mozilla.javascript.Context
import org.mozilla.javascript.ScriptableObject

internal interface ParameterInjector<T> {

    fun inject(scope: ScriptableObject, context: Context, input: T)

    fun set(scope: ScriptableObject, context: Context, name: String, value: Any?) {
        ScriptableObject.putConstProperty(
            scope,
            name,
            Context.javaToJS(value, scope, context)
        )
    }
}
