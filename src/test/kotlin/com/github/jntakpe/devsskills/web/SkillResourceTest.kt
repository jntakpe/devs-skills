package com.github.jntakpe.devsskills.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jntakpe.devsskills.model.Skill
import com.github.jntakpe.devsskills.model.SkillCategory
import com.github.jntakpe.devsskills.utils.EmployeeDAO
import com.github.jntakpe.devsskills.utils.SimpleSkillDTO
import io.restassured.RestAssured
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
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
        val skill = SimpleSkillDTO(SkillCategory.BACKEND.name, "Spring Boot")
        RestAssured.given(spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("skills_add",
                        preprocessRequest(modifyUris().port(8080), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("id").description("The employee identifier")),
                        requestFields(simpleSkillDTOFields()),
                        responseFields(employeeDetailDTOFields())))
                .body(ObjectMapper().writeValueAsString(skill))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .`when`().post(Urls.SKILLS_API, employee.id.toString())
                .then().statusCode(HttpStatus.OK.value())
                .apply {
                    validateEmployee(employee)
                    validateSkills(employee.copy(skills = employee.skills.toMutableSet()
                            .apply { add(Skill(SkillCategory.valueOf(skill.category), skill.name)) }))
                }
    }

}