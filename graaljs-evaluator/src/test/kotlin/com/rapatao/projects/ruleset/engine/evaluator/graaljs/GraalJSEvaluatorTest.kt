package com.rapatao.projects.ruleset.engine.evaluator.graaljs

import com.rapatao.projects.ruleset.engine.BaseEvaluatorTest
import com.rapatao.projects.ruleset.engine.helper.ExposeEngineTestOperator

class GraalJSEvaluatorTest : BaseEvaluatorTest(
    GraalJSEvaluator(
        operators = listOf(
            ExposeEngineTestOperator(),
        ),
    ),
)
