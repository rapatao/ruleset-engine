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
    )

    val inputData = RequestData(
        item = Item(price = BigDecimal.TEN)
    )

    fun allCases(): List<Arguments> = ExpressionCases.cases() + MatcherCases.cases()
}
