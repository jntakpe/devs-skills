package com.github.jntakpe.devsskills.dto

import com.github.jntakpe.devsskills.model.SkillCategory
import com.github.jntakpe.devsskills.model.Vote
import org.hibernate.validator.constraints.NotBlank
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

data class SkillDTO(val category: SkillCategory,
                    @field:NotBlank val name: String,
                    val votes: List<Vote> = emptyList(),
                    val mean: BigDecimal = ZERO)