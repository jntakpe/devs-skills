package com.github.jntakpe.devsskills.model

import java.math.BigDecimal

data class Skill(val category: SkillCategory, val name: String, val votes: List<Vote> = emptyList(), val mean: BigDecimal = BigDecimal.ZERO)