package com.github.jntakpe.devsskills.model

import com.github.jntakpe.devsskills.config.NoArg
import java.math.BigDecimal

@NoArg
data class Skill(val category: SkillCategory, val name: String, val votes: List<Vote> = emptyList(), val mean: BigDecimal = BigDecimal.ZERO)
