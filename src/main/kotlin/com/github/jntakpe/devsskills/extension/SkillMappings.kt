package com.github.jntakpe.devsskills.extension

import com.github.jntakpe.devsskills.dto.SkillDTO
import com.github.jntakpe.devsskills.model.Skill

fun SkillDTO.toEntity() = Skill(category, name, ratings.map { it.toEntity() }, mean)

fun Skill.toDTO() = SkillDTO(category, name, ratings.map { it.toDTO() }, mean)