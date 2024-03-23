package com.rapatao.projects.ruleset.engine.context

/**
 * The EvalEngine interface defines the contract for an evaluation engine that can execute
 * a block of code with given input data and return a result.
 * Implementations of this interface are responsible for providing the execution environment
 * and handling the invocation of the provided block of code.
 */
interface EvalEngine {

    /**
     * Executes the provided block of code with the given input data and returns a boolean value indicating
     * the success or failure of the execution.
     *
     * @param inputData The input data to be used in the execution.
     * @param block A lambda function that takes in a context and a scope as parameters and returns a boolean value.
     *              The context represents the context in which the execution takes place, and the scope represents
     *              the scope of the execution.
     * @return The result of the execution.
     */
    fun <T> call(inputData: Any, block: EvalContext.() -> T): T

    /**
     * Returns the name of the evaluation engine. This can be used to identify the engine in logging or debugging.
     *
     * @return The name of the evaluation engine.
     */
    fun name(): String
}
