package com.github.jntakpe.devsskills.utils

import com.github.jntakpe.devsskills.model.*
import com.github.jntakpe.devsskills.repository.EmployeeRepository
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.stereotype.Repository

@Repository
class EmployeeDAO(private val employeeRepository: EmployeeRepository, private val redisConnectionFactory: RedisConnectionFactory) {

    val jntakpe = Employee("jntakpe", "jntakpe@mail.com", "Jocelyn", "NTAKPE",
            setOf(Skill(SkillCategory.BACKEND, "Java", listOf(Rating(Level.EXPERT, "cbarillet"))),
                    Skill(SkillCategory.FRONTEND, "Angular", listOf(Rating(Level.ADVANCED, "cbarillet")))))
    val cbarillet = Employee("cbarillet", "cbarillet@mail.com", "Cyril", "BARILLET",
            setOf(Skill(SkillCategory.OPS, "Docker", listOf(Rating(Level.ADVANCED, "jntakpe")))))

    fun initDB() {
        redisConnectionFactory.connection.flushDb()
        employeeRepository.deleteAll()
                .thenMany(employeeRepository.insert(listOf(jntakpe, cbarillet)))
                .blockLast()
    }

    fun findAny() = employeeRepository.findAll().blockFirst() ?: throw IllegalStateException("No employee")

}