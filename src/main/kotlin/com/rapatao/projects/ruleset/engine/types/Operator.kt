package com.rapatao.projects.ruleset.engine.types

/**
 * This enum class defines a set of operators that can be utilized in different contexts such as string comparison,
 * numerical comparison, or array element verification.
 */
enum class Operator {
    /**
     * Represents the equality operator (==), used to check if two values are equal.
     */
    EQUALS,

    /**
     * Represents the inequality operator (!=), used to check if two values are not equal.
     */
    NOT_EQUALS,

    /**
     * Represents the greater than operator (>), used to compare if one value is greater than another.
     */
    GREATER_THAN,

    /**
     * Represents the greater than or equal to operator (>=), used to compare if one value is greater than or equal to
     * another.
     */
    GREATER_OR_EQUAL_THAN,

    /**
     * Represents the less than operator (<), used to compare if one value is less than another.
     */
    LESS_THAN,

    /**
     * Represents the less than or equal to operator (<=), used to compare if one value is less than or equal to
     * another.
     */
    LESS_OR_EQUAL_THAN,

    /**
     * Represents the operation to check if a string starts with a specified sequence of characters.
     */
    STARTS_WITH,

    /**
     * Represents the operation to check if a string ends with a specified sequence of characters.
     */
    ENDS_WITH,

    /**
     * Represents the operation to check if a string contains a specified sequence of characters or if an array contains
     * a particular element.
     */
    CONTAINS,
}
