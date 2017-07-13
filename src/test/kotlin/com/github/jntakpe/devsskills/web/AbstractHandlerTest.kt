package com.github.jntakpe.devsskills.web

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractHandlerTest {

    protected lateinit var spec: RequestSpecification
    @Rule @JvmField val restDocumentation = JUnitRestDocumentation()
    @LocalServerPort var port: Int = 0

    abstract fun initDB()

    @Before
    fun setUp() {
        initDB()
        RestAssured.port = port
        spec = RequestSpecBuilder().addFilter(RestAssuredRestDocumentation.documentationConfiguration(restDocumentation)).build()
    }

    protected fun errorDTOFields() = mutableListOf(
            PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("Error message").optional(),
            PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP status"),
            PayloadDocumentation.fieldWithPath("reason").type(JsonFieldType.STRING).description("Initial error cause")
    )
}