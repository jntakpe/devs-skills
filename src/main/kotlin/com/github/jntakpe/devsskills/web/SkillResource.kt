package com.github.jntakpe.devsskills.web

import com.github.jntakpe.devsskills.dto.SkillDTO
import com.github.jntakpe.devsskills.extension.toDetailDTO
import com.github.jntakpe.devsskills.extension.toEntity
import com.github.jntakpe.devsskills.extension.toId
import com.github.jntakpe.devsskills.service.SkillService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(Urls.SKILLS_API)
class SkillResource(private val skillService: SkillService) {

    @PostMapping
    fun addSkill(@PathVariable id: String, @RequestBody @Valid skill: SkillDTO) = skillService.addSkill(id.toId(), skill.toEntity())
            .map { it.toDetailDTO() }


}