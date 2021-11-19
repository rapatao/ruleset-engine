package com.rapatao.projects.ruleset.jackson

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.rapatao.projects.ruleset.engine.types.BetweenExpression
import com.rapatao.projects.ruleset.engine.types.BooleanExpression

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    value = [
        JsonSubTypes.Type(BooleanExpression::class),
        JsonSubTypes.Type(BetweenExpression::class),
    ]
)
interface ExpressionMixin
