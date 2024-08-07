package com.rapatao.projects.ruleset.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rapatao.projects.ruleset.engine.cases.TestData
import com.rapatao.projects.ruleset.engine.evaluator.kotlin.KotlinEvaluator
import com.rapatao.projects.ruleset.engine.types.Expression
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.allMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.anyMatch
import com.rapatao.projects.ruleset.engine.types.builder.MatcherBuilder.noneMatch
import com.rapatao.projects.ruleset.engine.types.builder.extensions.endsWith
import com.rapatao.projects.ruleset.engine.types.builder.extensions.equalsTo
import com.rapatao.projects.ruleset.engine.types.builder.extensions.expContains
import com.rapatao.projects.ruleset.engine.types.builder.extensions.greaterOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.greaterThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.isTrue
import com.rapatao.projects.ruleset.engine.types.builder.extensions.lessOrEqualThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.lessThan
import com.rapatao.projects.ruleset.engine.types.builder.extensions.notEqualsTo
import com.rapatao.projects.ruleset.engine.types.builder.extensions.startsWith
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.appendText
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.writeText

internal class SerializationExamplesBuilder {

    private val mapper = jacksonObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .addMixIn(Expression::class.java, ExpressionMixin::class.java)

    private val engine = KotlinEvaluator()

    private val cases = listOf(
        "* equalsTo: ",
        "field" equalsTo true,
        "field" equalsTo "true",
        "field" equalsTo 10,
        "field" equalsTo "\"value\"",
        "field" equalsTo "value",

        "* notEqualsTo: ",
        "field" notEqualsTo true,
        "field" notEqualsTo "true",
        "field" notEqualsTo 10,
        "field" notEqualsTo "\"value\"",
        "field" notEqualsTo "value",

        "* greaterThan: ",
        "field" greaterThan 10,

        "* greaterOrEqualThan: ",
        "field" greaterOrEqualThan 10,

        "* lessThan: ",
        "field" lessThan 10,

        "* lessOrEqualThan: ",
        "field" lessOrEqualThan 10,

        "* startsWith:",
        "field" startsWith "\"value\"",

        "* endsWith:",
        "field" endsWith "\"value\"",

        "* contains:",
        "field" expContains "\"value\"",

        "* allMatch: ",
        allMatch(
            "field".isTrue(),
            "price" lessThan 10.0,
        ),

        "* anyMatch: ",
        anyMatch(
            "field".isTrue(),
            "price" lessThan 10.0,
        ),

        "* noneMatch: ",
        noneMatch(
            "field".isTrue(),
            "price" lessThan 10.0,
        ),

        "* multiple group validation:",
        Expression(
            allMatch = listOf(
                "field".isTrue(),
                "price" lessThan 10.0,
            ),
            anyMatch = listOf(
                "field".isTrue(),
                "price" lessThan 10.0,
            ),
            noneMatch = listOf(
                "field".isTrue(),
                "price" lessThan 10.0,
            )
        ),
    )

    private val casesFromTests = TestData.cases()
        .flatMap { it.get().toList() }
        .filterIsInstance<Expression>()
        .filter {
            // remove from serialization example rules that one of the operands is null
            it.isValid(engine) &&
                (
                    (it.left != null && it.right != null) ||
                        !it.noneMatch.isNullOrEmpty() || !it.allMatch.isNullOrEmpty() || !it.anyMatch.isNullOrEmpty()
                    )
        }
        .toSet()

    @Test
    fun print() {
        val jsonMD = createJsonMD()

        jsonMD.appendRow("# JSON examples")
        jsonMD.appendRow("This file is automatically generated during the test phase.")

        val cls = this::class.java.name.replace(".", "/")

        jsonMD.appendRow("To see more details, check its source: [here](src/test/kotlin/${cls}.kt).")

        cases.forEach {
            jsonMD.appendRow(it)
        }

        jsonMD.appendRow(
            """
            ---
            
            ## Other examples used to validate the project
            
            """.trimIndent()
        )

        casesFromTests.forEach {
            jsonMD.appendRow(it)
        }
    }

    private fun createJsonMD(): Path {
        val jsonMD = Paths.get("../JSON.md")

        if (jsonMD.exists()) {
            jsonMD.writeText("")
        } else {
            jsonMD.createFile()
        }
        return jsonMD
    }

    private fun Path.appendRow(it: Any? = null) {
        if (it is Expression) {
            this.appendText("```json\n")
            this.appendText(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(it))
            this.appendText("\n")
            this.appendText("```\n")
            this.appendText("\n")
        } else {
            this.appendText("${it ?: ""}\n")
            this.appendText("\n")
        }
    }
}
