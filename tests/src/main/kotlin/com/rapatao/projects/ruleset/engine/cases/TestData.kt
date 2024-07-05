package com.rapatao.projects.ruleset.engine.cases

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
        val arrTags: Array<String>,
        val nullableStr: String? = null,
    )

    val inputData = RequestData(
        item = Item(
            name = "product name",
            price = BigDecimal.TEN,
            tags = listOf(
                "test", "brand-new"
            ),
            arrTags = arrayOf("in_array")
        )
    )

    fun onFailureCases(): List<Arguments> = (OnFailureCases.cases())

    fun cases() =
        ExpressionCases.cases() +
            MatcherCases.cases() +
            OperatorWithCases.cases() +
            ContainsCases.cases()
}
