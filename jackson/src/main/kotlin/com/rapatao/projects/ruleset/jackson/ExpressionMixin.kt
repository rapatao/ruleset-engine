package com.rapatao.projects.ruleset.jackson

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.rapatao.projects.ruleset.engine.types.OnFailure

/**
 * This interface represents a mixin for JSON serialization and deserialization of expressions.
 * It provides methods for handling onFailure, operator, and validity of an expression.
 *
 * @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
interface ExpressionMixin {

    @JsonFormat(
        with = [
            JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES,
            JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES
        ],
    )
    fun onFailure(): OnFailure

    @JsonIgnore
    fun isValid(): Boolean
}
