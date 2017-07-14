package com.github.jntakpe.devsskills.extension

import com.github.jntakpe.devsskills.dto.SkillDTO
import com.github.jntakpe.devsskills.model.Skill

fun SkillDTO.toEntity() = Skill(category, name, votes, mean)

fun Skill.toDTO() = SkillDTO(category, name, votes, mean)