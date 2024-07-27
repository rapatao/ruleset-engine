package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import com.rapatao.projects.ruleset.engine.BaseEvaluatorTest
import com.rapatao.projects.ruleset.engine.helper.ExposeEngineTestOperator

class KotlinEvaluatorTest : BaseEvaluatorTest(
    KotlinEvaluator(
        operators = listOf(
            ExposeEngineTestOperator(),
        ),
    ),
)
