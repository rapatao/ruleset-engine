package com.rapatao.projects.ruleset

import org.junit.jupiter.params.provider.Arguments
import java.math.BigDecimal

open class Scenarios {

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

    fun scenarios(): List<Arguments> = ExpressionCases.cases() + MatcherCases.cases()
}
