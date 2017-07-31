package com.github.jntakpe.devsskills.dto

import com.github.jntakpe.devsskills.model.SkillCategory
import org.hibernate.validator.constraints.NotBlank
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

data class SkillDTO(val category: SkillCategory,
                    @field:NotBlank val name: String,
                    val ratings: List<RatingDTO> = emptyList(),
                    val mean: BigDecimal = ZERO,
                    val id: String? = null)