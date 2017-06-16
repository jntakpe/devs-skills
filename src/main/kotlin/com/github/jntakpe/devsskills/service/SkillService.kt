package com.github.jntakpe.devsskills.service

import com.github.jntakpe.devsskills.model.Employee
import com.github.jntakpe.devsskills.model.Skill
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class SkillService(private val employeeService: EmployeeService) {

    val logger = LoggerFactory.getLogger(javaClass.simpleName)

    fun addSkill(id: ObjectId, skill: Skill): Mono<Employee> {
        return employeeService.findById(id)
                .doOnNext { logger.info("Adding skill {} to employee {}", skill, it) }
                .map { it.copy(skills = it.skills.toMutableSet().apply { add(skill) }) }
                .flatMap { employeeService.update(it, id) }
    }

}