package com.rapatao.projects.ruleset.engine.evaluator.graaljs

import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.context.EvalEngine
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.parameters.MapInjector
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.parameters.TypedInjector
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.Engine
import org.graalvm.polyglot.HostAccess
import org.graalvm.polyglot.Value

open class GraalJSEvalEngine(
    private val engine: Engine = Engine.newBuilder()
        .option("engine.WarnInterpreterOnly", "false")
        .build(),
    private val contextBuilder: Context.Builder = Context.newBuilder()
        .engine(engine)
        .option("js.ecmascript-version", "2023")
        .allowHostAccess(HostAccess.ALL).allowHostClassLookup { true }
        .option("js.nashorn-compat", "true").allowExperimentalOptions(true)
) : EvalEngine {

    override fun <T> call(inputData: Any, block: EvalContext.() -> T): T =
        createContext().let {
            parseParameters(
                it.getBindings("js"),
                inputData,
            )
            block(GraalJSContext(it))
        }

    private fun createContext(): Context {
        return contextBuilder.build()
    }

    override fun name(): String = "GraalJS"

    /**
     * Parses parameters and injects them into the given scope based on the input data.
     *
     * @param bindings the values object where the parameters will be injected
     * @param inputData the input data containing the parameters
     */
    open fun parseParameters(bindings: Value, inputData: Any) {
        when (inputData) {
            is Map<*, *> -> MapInjector.inject(bindings, inputData)
            else -> TypedInjector.inject(bindings, inputData)
        }
    }
}
