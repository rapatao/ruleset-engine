package com.rapatao.projects.ruleset.jackson

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.rapatao.projects.ruleset.engine.types.OnFailure
import com.rapatao.projects.ruleset.engine.types.Operator

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
interface ExpressionMixin {

    @JsonFormat(
        with = [
            JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES,
            JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES
        ],
    )
    fun onFailure(): OnFailure

    @JsonFormat(
        with = [
            JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES,
            JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES
        ],
    )
    fun operator(): Operator
}
