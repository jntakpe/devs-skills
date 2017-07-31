package com.github.jntakpe.devsskills.extension

import com.github.jntakpe.devsskills.dto.SkillDTO
import com.github.jntakpe.devsskills.model.Skill
import org.bson.types.ObjectId

fun SkillDTO.toEntity() = Skill(category, name, ratings.map { it.toEntity() }, mean, id?.toId() ?: ObjectId())

fun Skill.toDTO() = SkillDTO(category, name, ratings.map { it.toDTO() }, mean, id.toString())