package com.github.jntakpe.devsskills.service

import com.github.jntakpe.devsskills.exception.IdNotFoundException
import com.github.jntakpe.devsskills.model.Employee
import com.github.jntakpe.devsskills.model.Skill
import com.github.jntakpe.devsskills.model.SkillCategory.*
import com.github.jntakpe.devsskills.repository.EmployeeRepository
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.test
import java.time.Duration

@SpringBootTest
@RunWith(SpringRunner::class)
class EmployeeServiceTest {

    @Autowired lateinit var employeeRepository: EmployeeRepository
    @Autowired lateinit var employeeService: EmployeeService
    val jntakpe = Employee("jntakpe", "jntakpe@mail.com", "Jocelyn", "NTAKPE", setOf(Skill(BACKEND, "Java"), Skill(FRONTEND, "Angular")))
    val cbarillet = Employee("cbarillet", "cbarillet@mail.com", "Cyril", "BARILLET", setOf(Skill(OPS, "Docker")))

    @Before
    fun setUp() {
        employeeRepository.deleteAll()
                .thenMany(employeeRepository.insert(listOf(jntakpe, cbarillet)))
                .blockLast()
    }

    @Test
    fun `should find employee by id`() {
        employeeService.findById(jntakpe.id!!).test()
                .expectSubscription()
                .consumeNextWith {
                    assertThat(it).isNotNull()
                    assertThat(it.login).isEqualTo(jntakpe.login)
                }
                .verifyComplete()
    }

    @Test
    fun `should find employee by id 100 times below 1 sec`() {
        val execs = 100
        val duration = Flux.range(0, execs).flatMap { employeeService.findById(jntakpe.id!!) }.test()
                .expectSubscription()
                .expectNextCount(execs.toLong())
                .verifyComplete()
        assertThat(duration).isLessThan(Duration.ofSeconds(1))
    }

    @Test
    fun `should not find employee because id doesn't exist`() {
        employeeService.findById(ObjectId()).test()
                .expectSubscription()
                .expectNextCount(0L)
                .verifyComplete()
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

    @Test
    fun `should update employee`() {
        val updated = jntakpe.copy(login = "joss")
        employeeService.update(updated, jntakpe.id!!).test()
                .expectSubscription()
                .consumeNextWith {
                    assertThat(it.login).isEqualTo(updated.login)
                    assertThat(employeeRepository.findByLoginIgnoreCase(updated.login).block()).isNotNull()
                    assertThat(employeeRepository.findByLoginIgnoreCase(jntakpe.login).block()).isNull()
                }
                .verifyComplete()
    }

    @Test
    fun `should fail to update employee cuz wrong id`() {
        employeeService.update(jntakpe, ObjectId()).test()
                .expectSubscription()
                .verifyError(IdNotFoundException::class.java)
    }

}