package com.github.jntakpe.devsskills.web

import com.github.jntakpe.devsskills.extension.toDTO
import com.github.jntakpe.devsskills.model.Employee
import com.github.jntakpe.devsskills.model.SkillCategory
import io.restassured.response.ValidatableResponse
import org.hamcrest.CoreMatchers.*
import org.springframework.restdocs.payload.JsonFieldType.*
import org.springframework.restdocs.payload.PayloadDocumentation.applyPathPrefix
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath

fun validateEmployee(employee: Employee): ValidatableResponse.() -> Unit {
    return {
        body("id", notNullValue())
        body("login", equalTo(employee.login))
        body("email", equalTo(employee.email))
        body("firstName", equalTo(employee.firstName))
        body("lastName", equalTo(employee.lastName))
    }
}

fun validateSkills(employee: Employee): ValidatableResponse.() -> Unit {
    return {
        body("skills", notNullValue())
        body("skills", hasItems(employee.skills.map { it.toDTO() }))
    }
}

fun employeeDTOFields() = mutableListOf(
        fieldWithPath("id").type(STRING).description("Auto generated identifier"),
        fieldWithPath("login").type(STRING).description("Corporate username"),
        fieldWithPath("email").type(STRING).description("Corporate mail address"),
        fieldWithPath("firstName").type(STRING).description("First name"),
        fieldWithPath("lastName").type(STRING).description("Last name")
)

fun employeeDetailDTOFields() = employeeDTOFields().apply {
    add(fieldWithPath("skills").type(ARRAY).description("List of skills"))
    addAll(applyPathPrefix("skills[].", skillDTOFields()))
}

fun skillDTOFields() = mutableListOf(
        fieldWithPath("category").type(STRING)
                .description("Skill category. Accepted values : ${SkillCategory.values().joinToString(",")}"),
        fieldWithPath("name").type(STRING).description("Skill name"),
        fieldWithPath("votes").type(ARRAY).description("List of votes"),
        fieldWithPath("mean").type(NUMBER).description("Votes mean")
)