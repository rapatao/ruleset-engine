package com.rapatao.projects.ruleset

import com.rapatao.projects.ruleset.engine.Matcher
import com.rapatao.projects.ruleset.engine.Matcher.Companion.allMatch
import com.rapatao.projects.ruleset.engine.Matcher.Companion.anyMatch
import com.rapatao.projects.ruleset.engine.Matcher.Companion.expression
import com.rapatao.projects.ruleset.engine.Matcher.Companion.noneMatch
import com.rapatao.projects.ruleset.engine.expressions.IsBetween
import com.rapatao.projects.ruleset.engine.expressions.IsBetween.Companion.isFieldBetween
import com.rapatao.projects.ruleset.engine.expressions.IsEqualTo
import com.rapatao.projects.ruleset.engine.expressions.IsEqualTo.Companion.isFieldEquals
import com.rapatao.projects.ruleset.engine.expressions.IsTrue.Companion.isTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal

internal class EvaluatorTest {

    private val inputData = RequestData(
        item = Item(price = BigDecimal.TEN)
    )

    private val evaluator = Evaluator()

    companion object {
        @JvmStatic
        fun tests() = listOf(
            // 1
            Arguments.of(
                expression("item.price <= 1000"),
                true
            ),
            // 2
            Arguments.of(
                expression("item.price >= 1000"),
                false
            ),
            // 3
            Arguments.of(
                noneMatch(
                    expression("item.price >= 1000")
                ),
                true
            ),
            // 4
            Arguments.of(
                noneMatch(
                    expression("item.price <= 1000"),
                ),
                false
            ),
            // 5
            Arguments.of(
                noneMatch(
                    expression("item.price >= 1000"),
                    expression("item.price <= 1000"),
                ),
                false
            ),
            // 6
            Arguments.of(
                noneMatch(
                    expression("item.price >= 1000"),
                    expression("item.price >= 100"),
                ),
                true
            ),
            // 7
            Arguments.of(
                noneMatch(
                    expression("item.price <= 1000"),
                    expression("item.price <= 100"),
                ),
                false
            ),
            // 8
            Arguments.of(
                noneMatch(
                    noneMatch(
                        expression("item.price >= 1000"),
                    ),
                ),
                true
            ),
            // 9
            Arguments.of(
                noneMatch(
                    noneMatch(
                        expression("item.price <= 1000"),
                    ),
                ),
                false
            ),
            // 10
            Arguments.of(
                noneMatch(
                    noneMatch(
                        expression("item.price <= 1000"),
                    ),
                    expression("item.price >= 1000"),
                ),
                false
            ),
            // 11
            Arguments.of(
                noneMatch(
                    expression("item.price >= 1000"),
                    noneMatch(
                        expression("item.price <= 1000"),
                    ),
                ),
                false
            ),
            // 12
            Arguments.of(
                anyMatch(
                    expression("item.price >= 1000"),
                ),
                false
            ),
            // 13
            Arguments.of(
                anyMatch(
                    expression("item.price <= 1000"),
                ),
                true
            ),
            // 13
            Arguments.of(
                anyMatch(
                    anyMatch(
                        expression("item.price <= 1000"),
                    ),
                    expression("item.price >= 1000"),
                ),
                true
            ),
            // 15
            Arguments.of(
                anyMatch(
                    anyMatch(
                        expression("item.price >= 1000"),
                    ),
                    expression("item.price <= 1000"),
                ),
                true
            ),
            // 16
            Arguments.of(
                anyMatch(
                    noneMatch(
                        expression("item.price >= 1000"),
                    ),
                    expression("item.price <= 1000"),
                ),
                true
            ),
            // 17
            Arguments.of(
                anyMatch(
                    noneMatch(
                        expression("item.price <= 1000"),
                    ),
                    expression("item.price >= 1000"),
                ),
                true
            ),
            // 18
            Arguments.of(
                anyMatch(
                    noneMatch(
                        expression("item.price >= 1000"),
                    ),
                    expression("item.price >= 1000"),
                ),
                false
            ),
            // 19
            Arguments.of(
                allMatch(
                    expression("item.price >= 1000"),
                ),
                false
            ),
            // 20
            Arguments.of(
                allMatch(
                    expression("item.price <= 1000"),
                ),
                true
            ),
            // 21
            Arguments.of(
                allMatch(
                    allMatch(
                        expression("item.price <= 1000"),
                    ),
                    expression("item.price <= 1000"),
                ),
                true
            ),
            // 22
            Arguments.of(
                allMatch(
                    allMatch(
                        expression("item.price <= 1000"),
                    ),
                    noneMatch(
                        expression("item.price >= 1000"),
                    ),
                    expression("item.price <= 1000"),
                ),
                true
            ),
            // 23
            Arguments.of(
                allMatch(
                    allMatch(
                        expression("item.price <= 1000"),
                    ),
                    noneMatch(
                        expression("item.price >= 1000"),
                    ),
                    anyMatch(
                        noneMatch(
                            expression("item.price <= 1000"),
                        ),
                        expression("item.price >= 1000"),
                    ),
                    expression("item.price <= 1000"),
                ),
                true
            ),
            // 24
            Arguments.of(
                allMatch(
                    allMatch(
                        expression("item.price <= 1000"),
                    ),
                    noneMatch(
                        expression("item.price >= 1000"),
                    ),
                    anyMatch(
                        noneMatch(
                            expression("item.price >= 1000"),
                        ),
                        expression("item.price >= 1000"),
                    ),
                    expression("item.price <= 1000"),
                ),
                false
            ),
            // 25
            Arguments.of(
                Matcher(
                    allMatch = listOf(expression("item.price <= 1000")),
                    noneMatch = listOf(expression("item.price >= 1000")),
                    anyMatch = listOf(expression("item.price <= 1000")),
                    expression = isTrue("item.price <= 1000")
                ),
                true
            ),
            // 26
            Arguments.of(
                Matcher(
                    allMatch = listOf(expression("item.price <= 1000")),
                    noneMatch = listOf(expression("item.price >= 1000")),
                    anyMatch = listOf(expression("item.price <= 1000")),
                    expression = isTrue("item.price >= 1000")
                ),
                false
            ),
            // 27
            Arguments.of(
                expression(
                    isFieldBetween("item.price") to 1 and 1000
                ),
                true
            ),
            // 28
            Arguments.of(
                expression(
                    IsBetween("item.price", 1, 1000)
                ),
                true
            ),
            // 29
            Arguments.of(
                expression(
                    IsBetween("item.price", 100, 1000)
                ),
                false
            ),
            // 30
            Arguments.of(
                expression(
                    IsEqualTo("item.price", BigDecimal.TEN)
                ),
                true
            ),
            // 31
            Arguments.of(
                expression(
                    isFieldEquals("item.price") to BigDecimal.TEN
                ),
                true
            ),
            // 32
            Arguments.of(
                expression(
                    isFieldEquals("item.price") to BigDecimal.ZERO
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
    fun `runs the last test scenario`() {
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
data class Item(val price: BigDecimal)
