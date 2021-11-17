package com.rapatao.projects.ruleset

import com.rapatao.projects.ruleset.engine.Matcher
import com.rapatao.projects.ruleset.engine.Matcher.Companion.allMatch
import com.rapatao.projects.ruleset.engine.Matcher.Companion.anyMatch
import com.rapatao.projects.ruleset.engine.Matcher.Companion.isTrue
import com.rapatao.projects.ruleset.engine.Matcher.Companion.noneMatch
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal

internal class EvaluatorTest {

    private val inputData = RequestData(
        item = Item(price = BigDecimal.TEN, domainId = "<domain-id-1>")
    )

    private val evaluator = Evaluator()

    companion object {
        @JvmStatic
        fun tests() = listOf(
            // 1
            Arguments.of(
                isTrue("\$item.price <= 1000"),
                true
            ),
            // 2
            Arguments.of(
                isTrue("\$item.price >= 1000"),
                false
            ),
            // 3
            Arguments.of(
                noneMatch(
                    isTrue("\$item.price >= 1000")
                ),
                true
            ),
            // 4
            Arguments.of(
                noneMatch(
                    isTrue("\$item.price <= 1000"),
                ),
                false
            ),
            // 5
            Arguments.of(
                noneMatch(
                    isTrue("\$item.price >= 1000"),
                    isTrue("\$item.price <= 1000"),
                ),
                false
            ),
            // 6
            Arguments.of(
                noneMatch(
                    isTrue("\$item.price >= 1000"),
                    isTrue("\$item.price >= 100"),
                ),
                true
            ),
            // 7
            Arguments.of(
                noneMatch(
                    isTrue("\$item.price <= 1000"),
                    isTrue("\$item.price <= 100"),
                ),
                false
            ),
            // 8
            Arguments.of(
                noneMatch(
                    noneMatch(
                        isTrue("\$item.price >= 1000"),
                    ),
                ),
                true
            ),
            // 9
            Arguments.of(
                noneMatch(
                    noneMatch(
                        isTrue("\$item.price <= 1000"),
                    ),
                ),
                false
            ),
            // 10
            Arguments.of(
                noneMatch(
                    noneMatch(
                        isTrue("\$item.price <= 1000"),
                    ),
                    isTrue("\$item.price >= 1000"),
                ),
                false
            ),
            // 11
            Arguments.of(
                noneMatch(
                    isTrue("\$item.price >= 1000"),
                    noneMatch(
                        isTrue("\$item.price <= 1000"),
                    ),
                ),
                false
            ),
            // 12
            Arguments.of(
                anyMatch(
                    isTrue("\$item.price >= 1000"),
                ),
                false
            ),
            // 13
            Arguments.of(
                anyMatch(
                    isTrue("\$item.price <= 1000"),
                ),
                true
            ),
            // 13
            Arguments.of(
                anyMatch(
                    anyMatch(
                        isTrue("\$item.price <= 1000"),
                    ),
                    isTrue("\$item.price >= 1000"),
                ),
                true
            ),
            // 15
            Arguments.of(
                anyMatch(
                    anyMatch(
                        isTrue("\$item.price >= 1000"),
                    ),
                    isTrue("\$item.price <= 1000"),
                ),
                true
            ),
            // 16
            Arguments.of(
                anyMatch(
                    noneMatch(
                        isTrue("\$item.price >= 1000"),
                    ),
                    isTrue("\$item.price <= 1000"),
                ),
                true
            ),
            // 17
            Arguments.of(
                anyMatch(
                    noneMatch(
                        isTrue("\$item.price <= 1000"),
                    ),
                    isTrue("\$item.price >= 1000"),
                ),
                true
            ),
            // 18
            Arguments.of(
                anyMatch(
                    noneMatch(
                        isTrue("\$item.price >= 1000"),
                    ),
                    isTrue("\$item.price >= 1000"),
                ),
                false
            ),
            // 19
            Arguments.of(
                allMatch(
                    isTrue("\$item.price >= 1000"),
                ),
                false
            ),
            // 20
            Arguments.of(
                allMatch(
                    isTrue("\$item.price <= 1000"),
                ),
                true
            ),
            // 21
            Arguments.of(
                allMatch(
                    allMatch(
                        isTrue("\$item.price <= 1000"),
                    ),
                    isTrue("\$item.price <= 1000"),
                ),
                true
            ),
            // 22
            Arguments.of(
                allMatch(
                    allMatch(
                        isTrue("\$item.price <= 1000"),
                    ),
                    noneMatch(
                        isTrue("\$item.price >= 1000"),
                    ),
                    isTrue("\$item.price <= 1000"),
                ),
                true
            ),
            // 23
            Arguments.of(
                allMatch(
                    allMatch(
                        isTrue("\$item.price <= 1000"),
                    ),
                    noneMatch(
                        isTrue("\$item.price >= 1000"),
                    ),
                    anyMatch(
                        noneMatch(
                            isTrue("\$item.price <= 1000"),
                        ),
                        isTrue("\$item.price >= 1000"),
                    ),
                    isTrue("\$item.price <= 1000"),
                ),
                true
            ),
            // 24
            Arguments.of(
                allMatch(
                    allMatch(
                        isTrue("\$item.price <= 1000"),
                    ),
                    noneMatch(
                        isTrue("\$item.price >= 1000"),
                    ),
                    anyMatch(
                        noneMatch(
                            isTrue("\$item.price >= 1000"),
                        ),
                        isTrue("\$item.price >= 1000"),
                    ),
                    isTrue("\$item.price <= 1000"),
                ),
                false
            ),
            // 25
            Arguments.of(
                Matcher(
                    allMatch = listOf(isTrue("\$item.price <= 1000")),
                    noneMatch = listOf(isTrue("\$item.price >= 1000")),
                    anyMatch = listOf(isTrue("\$item.price <= 1000")),
                    isTrue = "\$item.price <= 1000"
                ),
                true
            ),
            // 26
            Arguments.of(
                Matcher(
                    allMatch = listOf(isTrue("\$item.price <= 1000")),
                    noneMatch = listOf(isTrue("\$item.price >= 1000")),
                    anyMatch = listOf(isTrue("\$item.price <= 1000")),
                    isTrue = "\$item.price >= 1000"
                ),
                false
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("tests")
    fun doTest(ruleSet: Matcher, expected: Boolean) {
        assertEquals(
            expected,
            evaluator.evaluate(rule = ruleSet, inputData = inputData)
        )
    }

    @Test
//    @Disabled
    fun temp() {
        val caseNumber = tests().size

        val cases: List<Arguments> = tests()
        val test = cases[caseNumber - 1].get()
        doTest(
            test[0] as Matcher,
            test[1] as Boolean
        )
    }

}

data class RequestData(val item: Item)
data class Item(val price: BigDecimal, val domainId: String?)