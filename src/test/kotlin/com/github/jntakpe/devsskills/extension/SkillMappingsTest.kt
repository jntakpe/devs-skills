package com.github.jntakpe.devsskills.extension

import com.github.jntakpe.devsskills.dto.RatingDTO
import com.github.jntakpe.devsskills.dto.SkillDTO
import com.github.jntakpe.devsskills.model.Level
import com.github.jntakpe.devsskills.model.Rating
import com.github.jntakpe.devsskills.model.Skill
import com.github.jntakpe.devsskills.model.SkillCategory
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigDecimal

class SkillMappingsTest {

    @Test
    fun `should map DTO to entity`() {
        val ratings = listOf(RatingDTO(Level.ADVANCED, "toto"), RatingDTO(Level.BEGINNER, "titi"))
        val dto = SkillDTO(SkillCategory.BACKEND, "Java", ratings, BigDecimal.ONE)
        val entity = dto.toEntity()
        assertThat(entity).isNotNull()
        assertThat(entity.category).isEqualTo(dto.category)
        assertThat(entity.name).isEqualTo(dto.name)
        assertThat(entity.ratings.map { it.level }).isEqualTo(dto.ratings.map { it.level })
        assertThat(entity.ratings.map { it.accessor }).isEqualTo(dto.ratings.map { it.accessor })
        assertThat(entity.mean).isEqualTo(dto.mean)
    }

    @Test
    fun `should map entity to DTO`() {
        val ratings = listOf(Rating(Level.EXPERT, "toto"), Rating(Level.ADVANCED, "titi"))
        val entity = Skill(SkillCategory.BACKEND, "Spring Boot", ratings, BigDecimal.ONE)
        val dto = entity.toDTO()
        assertThat(dto).isNotNull()
        assertThat(dto.category).isEqualTo(entity.category)
        assertThat(dto.name).isEqualTo(entity.name)
        assertThat(dto.ratings.map { it.level }).isEqualTo(entity.ratings.map { it.level })
        assertThat(dto.ratings.map { it.accessor }).isEqualTo(entity.ratings.map { it.accessor })
        assertThat(dto.mean).isEqualTo(entity.mean)
    }
}