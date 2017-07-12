package com.github.jntakpe.devsskills.utils

import com.github.jntakpe.devsskills.model.Employee
import com.github.jntakpe.devsskills.model.Skill
import com.github.jntakpe.devsskills.model.SkillCategory
import com.github.jntakpe.devsskills.repository.EmployeeRepository
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.stereotype.Repository

@Repository
class EmployeeDAO(private val employeeRepository: EmployeeRepository, private val redisConnectionFactory: RedisConnectionFactory) {

    val jntakpe = Employee("jntakpe", "jntakpe@mail.com", "Jocelyn", "NTAKPE",
            setOf(Skill(SkillCategory.BACKEND, "Java"), Skill(SkillCategory.FRONTEND, "Angular")))
    val cbarillet = Employee("cbarillet", "cbarillet@mail.com", "Cyril", "BARILLET",
            setOf(Skill(SkillCategory.OPS, "Docker")))

    fun initDB() {
        redisConnectionFactory.connection.flushDb()
        employeeRepository.deleteAll()
                .thenMany(employeeRepository.insert(listOf(jntakpe, cbarillet)))
                .blockLast()
    }

}