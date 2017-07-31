package com.github.jntakpe.devsskills.extension

import com.github.jntakpe.devsskills.dto.EmployeeDTO
import com.github.jntakpe.devsskills.dto.EmployeeDetailDTO
import com.github.jntakpe.devsskills.dto.SkillDTO
import com.github.jntakpe.devsskills.model.Employee
import com.github.jntakpe.devsskills.model.Skill
import com.github.jntakpe.devsskills.model.SkillCategory
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.Test

class EmployeeMappingsTest {

    @Test
    fun `should map DTO to entity`() {
        val dto = EmployeeDTO("jdoe", "jdoe@mail.com", "John", "Doe", ObjectId().toString())
        val entity = dto.toEntity()
        assertThat(entity).isNotNull()
        assertThat(entity.id).isEqualTo(ObjectId(dto.id))
        assertThat(entity.login).isEqualTo(dto.login)
        assertThat(entity.email).isEqualTo(dto.email)
        assertThat(entity.firstName).isEqualTo(dto.firstName)
        assertThat(entity.lastName).isEqualTo(dto.lastName)
        assertThat(entity.skills).isEmpty()
    }

    @Test
    fun `should map detail DTO to entity`() {
        val skills = setOf(SkillDTO(SkillCategory.BACKEND, "Java"), SkillDTO(SkillCategory.FRONTEND, "Angular"))
        val dto = EmployeeDetailDTO(ObjectId().toString(), "jdoe", "jdoe@mail.com", "John", "Doe", skills)
        val entity = dto.toEntity()
        assertThat(entity).isNotNull()
        assertThat(entity.id).isEqualTo(ObjectId(dto.id))
        assertThat(entity.login).isEqualTo(dto.login)
        assertThat(entity.email).isEqualTo(dto.email)
        assertThat(entity.firstName).isEqualTo(dto.firstName)
        assertThat(entity.lastName).isEqualTo(dto.lastName)
        assertThat(entity.skills).isNotEmpty.hasSize(2)
    }

    @Test
    fun `should map entity to DTO`() {
        val entity = Employee("jdoe", "jdoe@mail.com", "John", "Doe",
                setOf(Skill(SkillCategory.BACKEND, "Java")), ObjectId())
        val dto = entity.toDTO()
        assertThat(dto).isNotNull()
        assertThat(ObjectId(dto.id)).isEqualTo(entity.id)
        assertThat(dto.login).isEqualTo(entity.login)
        assertThat(dto.email).isEqualTo(entity.email)
        assertThat(dto.firstName).isEqualTo(entity.firstName)
        assertThat(dto.lastName).isEqualTo(entity.lastName)
    }

    @Test
    fun `should map entity to detail DTO`() {
        val skills = setOf(Skill(SkillCategory.BACKEND, "Java"), Skill(SkillCategory.FRONTEND, "Angular"))
        val entity = Employee("jdoe", "jdoe@mail.com", "John", "Doe", skills, ObjectId())
        val dto = entity.toDetailDTO()
        assertThat(dto).isNotNull()
        assertThat(ObjectId(dto.id)).isEqualTo(entity.id)
        assertThat(dto.login).isEqualTo(entity.login)
        assertThat(dto.email).isEqualTo(entity.email)
        assertThat(dto.firstName).isEqualTo(entity.firstName)
        assertThat(dto.lastName).isEqualTo(entity.lastName)
        assertThat(dto.skills).isNotEmpty.hasSize(2)
    }

}