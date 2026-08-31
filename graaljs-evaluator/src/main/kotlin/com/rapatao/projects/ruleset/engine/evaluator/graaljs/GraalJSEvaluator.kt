package com.rapatao.projects.ruleset.engine.evaluator.graaljs

import com.rapatao.projects.ruleset.engine.Evaluator
import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator.Contains
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator.EndsWith
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator.Equals
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator.GreaterOrEqualThan
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator.GreaterThan
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator.LessOrEqualThan
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator.LessThan
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator.NotContains
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator.NotEndsWith
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator.NotEquals
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator.NotStartsWith
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.operator.StartsWith
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.parameters.MapInjector
import com.rapatao.projects.ruleset.engine.evaluator.graaljs.parameters.TypedInjector
import com.rapatao.projects.ruleset.engine.types.operators.Operator
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.Engine
import org.graalvm.polyglot.HostAccess
import org.graalvm.polyglot.Value

/**
 * An evaluator engine implementation that uses GraalJS Engine for evaluating JavaScript code with customizable options.
 *
 * @property engine The GraalVM Polyglot Engine instance used by this evaluator engine.
 * @property contextBuilder A builder instance used to create a JavaScript Context with custom options and settings.
 * @property reuseContextPerThread When `false` (the default), every evaluation builds a new Context and closes it
 * afterwards. When `true`, each thread keeps one Context and reuses it across evaluations, which removes the context
 * construction cost from every call. See the README for the trade-offs of the reused mode.
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
        .option("js.nashorn-compat", "true").allowExperimentalOptions(true),
    operators: List<Operator> = listOf(),
    private val reuseContextPerThread: Boolean = false,
) : Evaluator(
    operators = listOf(
        Equals(),
        NotEquals(),
        GreaterThan(),
        GreaterOrEqualThan(),
        LessThan(),
        LessOrEqualThan(),
        StartsWith(),
        NotStartsWith(),
        EndsWith(),
        NotEndsWith(),
        Contains(),
        NotContains(),
    ) + operators,
) {

    private val threadContext = ThreadLocal.withInitial { contextBuilder.build() }

    override fun <T> call(inputData: Any, block: EvalContext.() -> T): T =
        if (reuseContextPerThread) {
            evaluateWith(threadContext.get(), inputData, block)
        } else {
            contextBuilder.build().use { evaluateWith(it, inputData, block) }
        }

    private fun <T> evaluateWith(context: Context, inputData: Any, block: EvalContext.() -> T): T {
        val scope = context.eval("js", "({})")

        parseParameters(scope, inputData)

        val bindings = context.getBindings("js")
        // an operator may evaluate another expression through EvalContext.engine(), which reuses this context
        val outerScope = bindings.getMember(INPUT_SCOPE)
        bindings.putMember(INPUT_SCOPE, scope)

        return try {
            block(GraalJSContext(this, context))
        } finally {
            outerScope?.let { bindings.putMember(INPUT_SCOPE, it) }
        }
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
     * The scope is a fresh JavaScript object created for the evaluation, and it is replaced on every call, so the
     * injected members are never visible to another evaluation.
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

    internal companion object {
        /**
         * Name of the global member holding the input data of the current evaluation.
         */
        const val INPUT_SCOPE = "__ruleset_input__"
    }
}
