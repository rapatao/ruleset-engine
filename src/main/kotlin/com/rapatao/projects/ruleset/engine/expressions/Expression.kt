package com.rapatao.projects.ruleset.engine.expressions

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.rapatao.projects.ruleset.engine.expressions.types.BooleanExpression
import com.rapatao.projects.ruleset.engine.expressions.types.IsBetween
import com.rapatao.projects.ruleset.engine.expressions.types.JsExpression

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    value = [
        JsonSubTypes.Type(BooleanExpression::class),
        JsonSubTypes.Type(IsBetween::class),
        JsonSubTypes.Type(JsExpression::class),
    ]
)
interface Expression {
    fun parse(): String
}
