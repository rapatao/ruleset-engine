package com.rapatao.projects.ruleset.engine.types

/**
 * Enum class representing the possible actions on failure.
 *
 * This enum class defines three choices that can be made when handling failures:
 * - [FALSE]: Indicates that the failure should be considered as a false result.
 * - [TRUE]: Indicates that the failure should be considered as a true result.
 * - [THROW]: Indicates that an exception should be thrown when a failure occurs.
 **/
enum class OnFailure {
    FALSE,
    TRUE,
    THROW
}
