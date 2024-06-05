package com.rapatao.projects.ruleset.engine.helper

import com.rapatao.projects.ruleset.engine.Evaluator
import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.context.EvalEngine
import com.rapatao.projects.ruleset.engine.types.Expression
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo

object Helper {

    fun doEvaluationTest(engine: EvalEngine, ruleSet: Expression, expected: Boolean) {
        val evaluator = Evaluator(engine = engine)

        assertThat(
            evaluator.evaluate(expression = ruleSet, inputData = TestData.inputData),
            equalTo(expected)
        )
    }

    fun compareMatcher(source: Expression, target: Expression) {
        target.takeIf { it.parseable() }?.run {
            assertThat(target.left, equalTo(source.left))
            assertThat(target.operator, equalTo(source.operator))
            assertThat(target.right, equalTo(source.right))
            assertThat(target.onFailure, equalTo(source.onFailure))
        }

        compareMatcherList(source.allMatch, target.allMatch)
        compareMatcherList(source.anyMatch, target.anyMatch)
        compareMatcherList(source.noneMatch, target.noneMatch)
    }

    private fun compareMatcherList(source: List<Expression>?, target: List<Expression>?) {
        assertThat(source?.size, equalTo(target?.size))

        source?.forEachIndexed { index, matcher ->
            compareMatcher(matcher, target!![index])
        }
    }
}
