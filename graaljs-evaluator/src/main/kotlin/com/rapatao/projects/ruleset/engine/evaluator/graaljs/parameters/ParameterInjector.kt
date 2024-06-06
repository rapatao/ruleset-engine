package com.rapatao.projects.ruleset.engine.evaluator.graaljs.parameters

import org.graalvm.polyglot.Value

internal interface ParameterInjector<T> {

    fun inject(bindings: Value, input: T)

    fun set(bindings: Value, name: String, value: Any?) {
        bindings.putMember(name, value)
    }
}
