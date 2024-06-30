package com.rapatao.projects.ruleset.engine.evaluator.graaljs

import com.rapatao.projects.ruleset.engine.Evaluator
import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.parameters.MapInjector
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.parameters.TypedInjector
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.Engine
import org.graalvm.polyglot.HostAccess
import org.graalvm.polyglot.Value

/**
 * An evaluator engine implementation that uses GraalJS Engine for evaluating JavaScript code with customizable options.
 *
 * @property engine The GraalVM Polyglot Engine instance used by this evaluator engine.
 * @property contextBuilder A builder instance used to create a JavaScript Context with custom options and settings.
 *
 * @see org.graalvm.polyglot.Context.Builder
 * @see org.graalvm.polyglot.Engine
 */
open class GraalJSEvaluator(
    private val engine: Engine = Engine.newBuilder()
        .option("engine.WarnInterpreterOnly", "false")
        .build(),
    private val contextBuilder: Context.Builder = Context.newBuilder()
        .engine(engine)
        .option("js.ecmascript-version", "2023")
        .allowHostAccess(HostAccess.ALL).allowHostClassLookup { true }
        .option("js.nashorn-compat", "true").allowExperimentalOptions(true)
) : Evaluator() {

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

    /**
     * Returns the name of this engine.
     *
     * @return the name of this engine
     */
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
