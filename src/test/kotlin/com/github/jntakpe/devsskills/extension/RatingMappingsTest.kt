package com.github.jntakpe.devsskills.extension

import com.github.jntakpe.devsskills.dto.RatingDTO
import com.github.jntakpe.devsskills.model.Level
import com.github.jntakpe.devsskills.model.Rating
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RatingMappingsTest {

    @Test
    fun `should map DTO to entity`() {
        val dto = RatingDTO(Level.ADVANCED, "jntakpe")
        val entity = dto.toEntity()
        assertThat(entity).isNotNull()
        assertThat(entity.level).isEqualTo(dto.level)
        assertThat(entity.accessor).isEqualTo(dto.accessor)
    }

    @Test
    fun `should map entity to DTO`() {
        val entity = Rating(Level.ADVANCED, "jntakpe")
        val dto = entity.toDTO()
        assertThat(dto).isNotNull()
        assertThat(dto.level).isEqualTo(entity.level)
        assertThat(dto.accessor).isEqualTo(entity.accessor)
    }
}