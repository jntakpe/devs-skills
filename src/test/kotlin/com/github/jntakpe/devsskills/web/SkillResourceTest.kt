package com.github.jntakpe.devsskills.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jntakpe.devsskills.dto.SkillDTO
import com.github.jntakpe.devsskills.extension.toEntity
import com.github.jntakpe.devsskills.model.SkillCategory
import com.github.jntakpe.devsskills.utils.EmployeeDAO
import io.restassured.RestAssured
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.restassured3.operation.preprocess.RestAssuredPreprocessors.modifyUris

class SkillResourceTest : AbstractResourceTest() {

    @Autowired lateinit var employeeDAO: EmployeeDAO

    override fun initDB() {
        employeeDAO.initDB()
    }

    @Test
    fun `should add skill`() {
        val employee = employeeDAO.findAny()
        val skill = SkillDTO(SkillCategory.BACKEND, "Spring Boot")
        RestAssured.given(spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("skills_add",
                        preprocessRequest(modifyUris().port(8080), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(employeeDetailDTOFields())))
                .body(ObjectMapper().writeValueAsString(skill))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .`when`().post(Urls.SKILLS_API, employee.id.toString())
                .then().statusCode(HttpStatus.OK.value())
                .apply {
                    validateEmployee(employee)
                    validateSkills(employee.copy(skills = employee.skills.toMutableSet().apply { add(skill.toEntity()) }))
                }
    }

}