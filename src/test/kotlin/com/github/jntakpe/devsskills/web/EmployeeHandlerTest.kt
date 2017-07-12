package com.github.jntakpe.devsskills.web

import com.github.jntakpe.devsskills.model.Employee
import com.github.jntakpe.devsskills.utils.EmployeeDAO
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeHandlerTest {

    val baseUrl = "/api/employees"
    lateinit var spec: RequestSpecification
    @Rule @JvmField val restDocumentation = JUnitRestDocumentation()
    @LocalServerPort var port: Int = 0
    @Autowired lateinit var employeeDAO: EmployeeDAO

    @Before
    fun setUp() {
        employeeDAO.initDB()
        RestAssured.port = port
        spec = RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation)).build()
    }

    @Test
    fun `should find employee using login`() {
        RestAssured.given(spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .`when`().get("$baseUrl/login/{login}", employeeDAO.jntakpe.login)
                .then().statusCode(HttpStatus.OK.value()).apply(validateEmployee(employeeDAO.jntakpe))
    }

    @Test
    fun `should not find employee using login`() {
        RestAssured.given(spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
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

}