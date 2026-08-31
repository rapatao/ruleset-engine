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
import com.rapatao.projects.ruleset.engine.evaluator.rhino.operator.NotEndsWith
import com.rapatao.projects.ruleset.engine.evaluator.rhino.operator.NotEquals
import com.rapatao.projects.ruleset.engine.evaluator.rhino.operator.NotStartsWith
import com.rapatao.projects.ruleset.engine.evaluator.rhino.operator.StartsWith
import com.rapatao.projects.ruleset.engine.evaluator.rhino.parameters.MapInjector
import com.rapatao.projects.ruleset.engine.evaluator.rhino.parameters.TypedInjector
import com.rapatao.projects.ruleset.engine.types.operators.Operator
import org.mozilla.javascript.Context
import org.mozilla.javascript.ScriptableObject

/**
 * An evaluator engine implementation that uses Mozilla Rhino to evaluate JavaScript contexts with customizable options.
 *
 * The JavaScript standard objects are built once per evaluator instance and sealed, and every evaluation runs in a
 * cheap child scope that has them as its prototype. Input data is injected into that child scope, so bindings are
 * never shared between evaluations and the standard objects are never mutated, which keeps `evaluate` safe to call
 * concurrently.
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
        NotStartsWith(),
        EndsWith(),
        NotEndsWith(),
        Contains(),
        NotContains(),
    ) + operators,
) {

    /**
     * The sealed JavaScript standard objects shared by every evaluation of this evaluator.
     */
    private val sharedScope: ScriptableObject =
        contextFactory.call { context -> context.initSafeStandardObjects(null, true) }

    override fun <T> call(
        inputData: Any,
        block: (context: EvalContext) -> T
    ): T {
        return contextFactory.call { context ->
            val scope = context.newObject(sharedScope) as ScriptableObject
            scope.prototype = sharedScope
            // makes the child the top scope of the chain, so globals defined by a rule die with the evaluation
            scope.parentScope = null

            parseParameters(scope, context, inputData)

            block(RhinoContext(this, context, scope))
        }
    }

    override fun name(): String = "RhinoEval"

    /**
     * Parses parameters and injects them into the given scope based on the input data.
     *
     * The scope is a fresh child of the shared standard objects, created for this evaluation only, so the injected
     * properties are never visible to another evaluation.
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
