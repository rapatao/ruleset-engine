package com.rapatao.projects.ruleset.engine.evaluator.rhino

import com.rapatao.projects.ruleset.engine.Evaluator
import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.evaluator.rhino.operator.Contains
import com.rapatao.projects.ruleset.engine.evaluator.rhino.operator.EndsWith
import com.rapatao.projects.ruleset.engine.evaluator.rhino.operator.Equals
import com.rapatao.projects.ruleset.engine.evaluator.rhino.operator.GreaterOrEqualThan
import com.rapatao.projects.ruleset.engine.evaluator.rhino.operator.GreaterThan
import com.rapatao.projects.ruleset.engine.evaluator.rhino.operator.LessOrEqualThan
import com.rapatao.projects.ruleset.engine.evaluator.rhino.operator.LessThan
import com.rapatao.projects.ruleset.engine.evaluator.rhino.operator.NotContains
import com.rapatao.projects.ruleset.engine.evaluator.rhino.operator.NotEquals
import com.rapatao.projects.ruleset.engine.evaluator.rhino.operator.StartsWith
import com.rapatao.projects.ruleset.engine.evaluator.rhino.parameters.MapInjector
import com.rapatao.projects.ruleset.engine.evaluator.rhino.parameters.TypedInjector
import com.rapatao.projects.ruleset.engine.types.operators.Operator
import org.mozilla.javascript.Context
import org.mozilla.javascript.ScriptableObject

/**
 * An evaluator engine implementation that uses Mozilla Rhino to evaluate JavaScript contexts with customizable options.
 */
open class RhinoEvaluator(
    private val contextFactory: RhinoContextFactory = RhinoContextFactory(),
    operators: List<Operator> = listOf(),
) : Evaluator(
    operators = listOf(
        Equals(),
        NotEquals(),
        GreaterThan(),
        GreaterOrEqualThan(),
        LessThan(),
        LessOrEqualThan(),
        StartsWith(),
        EndsWith(),
        Contains(),
        NotContains(),
    ) + operators,
) {

    override fun <T> call(
        inputData: Any,
        block: (context: EvalContext) -> T
    ): T {
        return contextFactory.call { context ->
            val scope = context.initSafeStandardObjects()

            parseParameters(scope, context, inputData)

            block(RhinoContext(this, context, scope))
        }
    }

    override fun name(): String = "RhinoEval"

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
