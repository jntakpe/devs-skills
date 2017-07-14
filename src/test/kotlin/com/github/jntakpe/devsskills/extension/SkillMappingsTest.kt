package com.github.jntakpe.devsskills.extension

import com.github.jntakpe.devsskills.dto.SkillDTO
import com.github.jntakpe.devsskills.model.Skill
import com.github.jntakpe.devsskills.model.SkillCategory
import com.github.jntakpe.devsskills.model.Vote
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigDecimal

class SkillMappingsTest {

    @Test
    fun `should map DTO to entity`() {
        val dto = SkillDTO(SkillCategory.BACKEND, "Java", listOf(Vote.BEGINNER, Vote.INTERMEDIATE), BigDecimal.ONE)
        val entity = dto.toEntity()
        assertThat(entity).isNotNull()
        assertThat(entity.category).isEqualTo(dto.category)
        assertThat(entity.name).isEqualTo(dto.name)
        assertThat(entity.votes).isEqualTo(dto.votes)
        assertThat(entity.mean).isEqualTo(dto.mean)
    }

    @Test
    fun `should map entity to DTO`() {
        val entity = Skill(SkillCategory.BACKEND, "Spring Boot", listOf(Vote.ADVANCED, Vote.EXPERT), BigDecimal.ONE)
        val dto = entity.toDTO()
        assertThat(dto).isNotNull()
        assertThat(dto.category).isEqualTo(entity.category)
        assertThat(dto.name).isEqualTo(entity.name)
        assertThat(dto.votes).isEqualTo(entity.votes)
        assertThat(dto.mean).isEqualTo(entity.mean)
    }
}