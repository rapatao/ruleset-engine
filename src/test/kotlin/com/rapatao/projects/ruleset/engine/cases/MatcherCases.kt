package com.rapatao.projects.ruleset.engine.cases

import com.rapatao.projects.ruleset.engine.types.Matcher
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.allMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.anyMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.expression
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.noneMatch
import com.rapatao.projects.ruleset.engine.types.builder.ExpressionBuilder.isTrue
import org.junit.jupiter.params.provider.Arguments

object MatcherCases {

    @Suppress("LongMethod")
    fun cases(): List<Arguments> = listOf(
        Arguments.of(
            noneMatch(
                expression("item.price >= 1000")
            ),
            true
        ),
        Arguments.of(
            noneMatch(
                expression("item.price <= 1000"),
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                expression("item.price >= 1000"),
                expression("item.price <= 1000"),
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                expression("item.price >= 1000"),
                expression("item.price >= 100"),
            ),
            true
        ),
        Arguments.of(
            noneMatch(
                expression("item.price <= 1000"),
                expression("item.price <= 100"),
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                noneMatch(
                    expression("item.price >= 1000"),
                ),
            ),
            true
        ),
        Arguments.of(
            noneMatch(
                noneMatch(
                    expression("item.price <= 1000"),
                ),
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                noneMatch(
                    expression("item.price <= 1000"),
                ),
                expression("item.price >= 1000"),
            ),
            false
        ),
        Arguments.of(
            noneMatch(
                expression("item.price >= 1000"),
                noneMatch(
                    expression("item.price <= 1000"),
                ),
            ),
            false
        ),
        Arguments.of(
            anyMatch(
                expression("item.price >= 1000"),
            ),
            false
        ),
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
        Arguments.of(
            anyMatch(
                anyMatch(
                    expression("item.price >= 1000"),
                ),
                expression("item.price <= 1000"),
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                noneMatch(
                    expression("item.price >= 1000"),
                ),
                expression("item.price <= 1000"),
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                noneMatch(
                    expression("item.price <= 1000"),
                ),
                expression("item.price >= 1000"),
            ),
            true
        ),
        Arguments.of(
            anyMatch(
                noneMatch(
                    expression("item.price >= 1000"),
                ),
                expression("item.price >= 1000"),
            ),
            false
        ),
        Arguments.of(
            allMatch(
                expression("item.price >= 1000"),
            ),
            false
        ),
        Arguments.of(
            allMatch(
                expression("item.price <= 1000"),
            ),
            true
        ),
        Arguments.of(
            allMatch(
                allMatch(
                    expression("item.price <= 1000"),
                ),
                expression("item.price <= 1000"),
            ),
            true
        ),
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
        Arguments.of(
            Matcher(
                allMatch = listOf(expression("item.price <= 1000")),
                noneMatch = listOf(expression("item.price >= 1000")),
                anyMatch = listOf(expression("item.price <= 1000")),
                expression = isTrue("item.price <= 1000")
            ),
            true
        ),
        Arguments.of(
            Matcher(
                allMatch = listOf(expression("item.price <= 1000")),
                noneMatch = listOf(expression("item.price >= 1000")),
                anyMatch = listOf(expression("item.price <= 1000")),
                expression = isTrue("item.price >= 1000")
            ),
            false
        ),
        Arguments.of(
            expression(true),
            true
        ),
    )
}
