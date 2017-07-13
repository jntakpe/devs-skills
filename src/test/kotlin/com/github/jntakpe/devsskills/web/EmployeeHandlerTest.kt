package com.github.jntakpe.devsskills.web

import com.github.jntakpe.devsskills.model.Employee
import com.github.jntakpe.devsskills.utils.EmployeeDAO
import io.restassured.RestAssured
import io.restassured.response.ValidatableResponse
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType.STRING
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.restassured3.operation.preprocess.RestAssuredPreprocessors.modifyUris

class EmployeeHandlerTest : AbstractHandlerTest() {

    val baseUrl = "/api/employees"
    @Autowired lateinit var employeeDAO: EmployeeDAO

    override fun initDB() {
        employeeDAO.initDB()
    }

    @Test
    fun `should find employee using login`() {
        RestAssured.given(spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("employees_findByLogin",
                        preprocessRequest(modifyUris().port(8080)),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("login").description("The employee login")),
                        responseFields(employeeDTOFields())))
                .`when`().get("$baseUrl/login/{login}", employeeDAO.jntakpe.login)
                .then().statusCode(HttpStatus.OK.value()).apply(validateEmployee(employeeDAO.jntakpe))
    }

    @Test
    fun `should not find employee using login`() {
        RestAssured.given(spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("employees_findByLogin_fail",
                        preprocessRequest(modifyUris().port(8080)),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("login").description("The employee login")),
                        responseFields(errorDTOFields())))
                .`when`().get("$baseUrl/login/{login}", "unknown")
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
    }

    private fun validateEmployee(employee: Employee): ValidatableResponse.() -> Unit {
        return {
            body("id", CoreMatchers.notNullValue())
            body("login", CoreMatchers.equalTo(employee.login))
            body("email", CoreMatchers.equalTo(employee.email))
            body("firstName", CoreMatchers.equalTo(employee.firstName))
            body("lastName", CoreMatchers.equalTo(employee.lastName))
        }
    }

    private fun employeeDTOFields() = mutableListOf(
            fieldWithPath("id").type(STRING).description("Auto generated identifier"),
            fieldWithPath("login").type(STRING).description("Corporate username"),
            fieldWithPath("email").type(STRING).description("Corporate mail address"),
            fieldWithPath("firstName").type(STRING).description("First name"),
            fieldWithPath("lastName").type(STRING).description("Last name")
    )

}