package com.rapatao.projects.ruleset.engine.types

/**
 * Enum class for different operators.
 *
 * This enum class represents various operators that can be used for comparison or evaluation operations.
 * The available operators are:
 * - EQUALS:     Represents the equals operator (==).
 * - NOT_EQUALS: Represents the not equals operator (!=).
 * - GREATER_THAN: Represents the greater than operator (>).
 * - GREATER_OR_EQUAL_THAN: Represents the greater than or equal to operator (>=).
 * - LESS_THAN: Represents the less than operator (<).
 * - LESS_OR_EQUAL_THAN: Represents the less than or equal to operator (<=).
 */
enum class Operator {
    EQUALS,
    NOT_EQUALS,
    GREATER_THAN,
    GREATER_OR_EQUAL_THAN,
    LESS_THAN,
    LESS_OR_EQUAL_THAN,
}
