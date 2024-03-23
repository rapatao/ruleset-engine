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
        val name: String,
        val tags: List<String>,
    )

    val inputData = RequestData(
        item = Item(
            name = "product name",
            price = BigDecimal.TEN,
            tags = listOf(
                "test", "brand-new"
            )
        )
    )

    fun engines(): List<Arguments> = listOf(Arguments.of(RhinoEvalEngine()))

    fun allCases(): List<Arguments> = getCases().flatMap {
        engines().map { engine ->
            Arguments.of(engine.get().first { it is EvalEngine }, *it.get())
        }
    }

    fun onFailureCases(): List<Arguments> = (OnFailureCases.cases()).flatMap {
        engines().map { engine ->
            Arguments.of(engine.get().first { it is EvalEngine }, *it.get())
        }
    }

    private fun getCases() =
        ExpressionCases.cases() +
            MatcherCases.cases() +
            OperatorWithCases.cases() +
            ContainsCases.cases()
}
