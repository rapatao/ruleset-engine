package com.rapatao.projects.ruleset.engine.evaluator.rhino

import com.rapatao.projects.ruleset.engine.BaseEvaluatorTest
import com.rapatao.projects.ruleset.engine.helper.ExposeEngineTestOperator

class RhinoEvaluatorTest : BaseEvaluatorTest(
    RhinoEvaluator(
        operators = listOf(
            ExposeEngineTestOperator(),
        ),
    ),
)
