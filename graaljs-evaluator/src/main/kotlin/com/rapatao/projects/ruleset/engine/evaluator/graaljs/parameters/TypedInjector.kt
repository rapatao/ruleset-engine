package com.rapatao.projects.ruleset.engine.evaluator.graaljs.parameters

import org.graalvm.polyglot.Value
import kotlin.reflect.full.memberProperties

internal data object TypedInjector : ParameterInjector<Any> {

    override fun inject(bindings: Value, input: Any) {
        input.javaClass.kotlin.memberProperties.forEach {
            set(bindings, it.name, it.get(input))
        }
    }
}
