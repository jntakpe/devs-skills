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
class EmployeeServiceTest {

    @Autowired lateinit var employeeRepository: EmployeeRepository
    @Autowired lateinit var employeeService: EmployeeService
    val jntakpe = Employee("jntakpe", "jntakpe@mail.com", "Jocelyn", "NTAKPE", listOf(Skill(BACKEND, "Java"), Skill(FRONTEND, "Angular")))
    val cbarillet = Employee("cbarillet", "cbarillet@mail.com", "Cyril", "BARILLET", listOf(Skill(OPS, "Docker")))

    @Before
    fun setUp() {
        employeeRepository.deleteAll()
                .thenMany(employeeRepository.insert(listOf(jntakpe, cbarillet)))
                .blockLast()
    }

    @Test
    fun `should find employee by login`() {
        employeeService.findByLogin(jntakpe.login).test()
                .expectSubscription()
                .consumeNextWith {
                    assertThat(it).isNotNull()
                    assertThat(it.login).isEqualTo(jntakpe.login)
                }
                .verifyComplete()
    }

    @Test
    fun `should find employee by login ignoring case`() {
        employeeService.findByLogin(jntakpe.login.toUpperCase()).test()
                .expectSubscription()
                .consumeNextWith { assertThat(it.login).isEqualTo(jntakpe.login) }
                .verifyComplete()
    }

    @Test
    fun `should not find employee because login doesn't exist`() {
        employeeService.findByLogin("unknown").test()
                .expectSubscription()
                .expectNextCount(0L)
                .verifyComplete()
    }

    @Test
    fun `should find employee by mail`() {
        employeeService.findByEmail(jntakpe.email).test()
                .expectSubscription()
                .consumeNextWith {
                    assertThat(it).isNotNull()
                    assertThat(it.email).isEqualTo(jntakpe.email)
                }
                .verifyComplete()
    }

    @Test
    fun `should find employee by mail ignoring case`() {
        employeeService.findByEmail(jntakpe.email.toUpperCase()).test()
                .expectSubscription()
                .consumeNextWith { assertThat(it.email).isEqualTo(jntakpe.email) }
                .verifyComplete()
    }

    @Test
    fun `should not find employee because mail doesn't exist`() {
        employeeService.findByEmail("unknown").test()
                .expectSubscription()
                .expectNextCount(0L)
                .verifyComplete()
    }

}