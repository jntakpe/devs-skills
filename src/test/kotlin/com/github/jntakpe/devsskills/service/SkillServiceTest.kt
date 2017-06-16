package com.github.jntakpe.devsskills.service

import com.github.jntakpe.devsskills.model.Employee
import com.github.jntakpe.devsskills.model.Skill
import com.github.jntakpe.devsskills.model.SkillCategory.*
import com.github.jntakpe.devsskills.repository.EmployeeRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.test

@SpringBootTest
@RunWith(SpringRunner::class)
class SkillServiceTest {

    @Autowired lateinit var employeeRepository: EmployeeRepository
    @Autowired lateinit var skillService: SkillService
    val jntakpe = Employee("jntakpe", "jntakpe@mail.com", "Jocelyn", "NTAKPE", setOf(Skill(BACKEND, "Java"), Skill(FRONTEND, "Angular")))
    val cbarillet = Employee("cbarillet", "cbarillet@mail.com", "Cyril", "BARILLET", setOf(Skill(OPS, "Docker")))
    val noskill = Employee("noskill", "noskill@mail.com", "No", "SKILL", emptySet())

    @Before
    fun setUp() {
        employeeRepository.deleteAll()
                .thenMany(employeeRepository.insert(listOf(jntakpe, cbarillet, noskill)))
                .blockLast()
    }

    @Test
    fun `should add skill to existing skill list`() {
        val mongo = Skill(DATABASE, "MongoDB")
        skillService.addSkill(jntakpe.id!!, mongo).test()
                .expectSubscription()
                .consumeNextWith {
                    assertThat(it.skills).contains(mongo)
                    assertThat(it.skills.size).isEqualTo(jntakpe.skills.size + 1)
                }
                .verifyComplete()
    }

    @Test
    fun `should add skill to empty skill list`() {
        skillService.addSkill(noskill.id!!, Skill(OPS, "Chef")).test()
                .expectSubscription()
                .consumeNextWith { assertThat(it.skills).hasSize(1) }
                .verifyComplete()
    }

    @Test
    fun `should not add skill cuz skill already exists`() {
        skillService.addSkill(cbarillet.id!!, Skill(OPS, "Docker")).test()
                .expectSubscription()
                .consumeNextWith { assertThat(it.skills).hasSize(cbarillet.skills.size) }
                .verifyComplete()
    }

}