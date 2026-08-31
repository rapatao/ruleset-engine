package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import com.rapatao.projects.ruleset.engine.Evaluator
import com.rapatao.projects.ruleset.engine.context.EvalContext
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.Contains
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.EndsWith
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.Equals
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.GreaterOrEqualThan
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.GreaterThan
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.LessOrEqualThan
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.LessThan
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.NotContains
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.NotEndsWith
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.NotEquals
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.NotStartsWith
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.operator.StartsWith
import com.rapatao.projects.ruleset.engine.types.operators.Operator

/**
 * An evaluator engine implementation that uses Kotlin to process expressions.
 *
 * Supported types: Java primitive types, boolean, string, number types, maps, lists and arrays.
 */
open class KotlinEvaluator(
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

    override fun <T> call(inputData: Any, block: (context: EvalContext) -> T): T = block(KotlinContext(this, inputData))

    override fun name(): String = "KotlinEval"
}
