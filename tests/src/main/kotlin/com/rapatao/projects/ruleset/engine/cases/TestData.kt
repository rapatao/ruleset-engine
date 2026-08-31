package com.rapatao.projects.ruleset.engine.cases

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
        val weight: BigDecimal = BigDecimal("1.5"),
        val scaled: BigDecimal = BigDecimal("10.00"),
        val quantities: List<Int> = listOf(1, 2),
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

    /**
     * The same [inputData] item under a deliberately wide root: [size] extra scalar fields and a [size] element list.
     *
     * Every rule in [cases] roots at `item.*`, so the suite runs unchanged against it and the only difference is how
     * much input surrounds the fields the rules read.
     */
    fun wideInput(size: Int): Map<String, Any?> =
        mapOf("item" to inputData.item) +
            (1..size).associate { "pad$it" to "value$it" } +
            mapOf("padList" to (1..size).map { "element$it" })

    fun cases() =
        ExpressionCases.cases() +
            MatcherCases.cases() +
            OperatorWithCases.cases() +
            ContainsCases.cases() +
            NumberCases.cases()
}
