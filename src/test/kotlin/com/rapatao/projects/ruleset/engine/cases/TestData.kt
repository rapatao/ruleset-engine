package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.context.EvalEngine
import com.rapatao.projects.ruleset.engine.evaluator.rhino.RhinoEvalEngine
import org.junit.jupiter.params.provider.Arguments
import java.math.BigDecimal

object TestData {

    data class RequestData(
        val item: Item,
    )

    data class Item(
        val price: BigDecimal,
        val trueValue: Boolean = true,
        val falseValue: Boolean = false,
    )

    val inputData = RequestData(
        item = Item(price = BigDecimal.TEN)
    )

    fun engines(): List<Arguments> = listOf(Arguments.of(RhinoEvalEngine()))

    fun allCases(): List<Arguments> = (ExpressionCases.cases() + MatcherCases.cases()).flatMap {
        engines().map { engine ->
            Arguments.of(engine.get().first { it is EvalEngine }, *it.get())
        }
    }

    fun onFailureCases(): List<Arguments> = (OnFailureCases.cases()).flatMap {
        engines().map { engine ->
            Arguments.of(engine.get().first { it is EvalEngine }, *it.get())
        }
    }
}
