package com.github.jntakpe.devsskills.web

import com.github.jntakpe.devsskills.utils.EmployeeDAO
import io.restassured.RestAssured
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.restassured3.operation.preprocess.RestAssuredPreprocessors.modifyUris

class EmployeeResourceTest : AbstractResourceTest() {

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
                        responseFields(employeeDetailDTOFields())))
                .`when`().get("${Urls.EMPLOYEES_API}/login/{login}", employeeDAO.jntakpe.login)
                .then().statusCode(HttpStatus.OK.value())
                .apply {
                    validateEmployee(employeeDAO.jntakpe)
                    validateSkills(employeeDAO.jntakpe)
                }
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
                .`when`().get("${Urls.EMPLOYEES_API}/login/{login}", "unknown")
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
    }

}