package com.rapatao.projects.ruleset.engine.evaluator.graaljs.parameters

import org.graalvm.polyglot.Value

internal data object MapInjector : ParameterInjector<Map<*, *>> {

    override fun inject(bindings: Value, input: Map<*, *>) {
        input.forEach {
            set(bindings, it.key.toString(), it.value)
        }
    }
}
