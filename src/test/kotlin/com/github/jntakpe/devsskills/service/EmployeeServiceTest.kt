package com.github.jntakpe.devsskills.service

import com.github.jntakpe.devsskills.exception.IdNotFoundException
import com.github.jntakpe.devsskills.model.Employee
import com.github.jntakpe.devsskills.repository.EmployeeRepository
import com.github.jntakpe.devsskills.utils.EmployeeDAO
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.test
import reactor.core.scheduler.Schedulers
import java.time.Duration

@SpringBootTest
@RunWith(SpringRunner::class)
class EmployeeServiceTest {

    @Autowired lateinit var employeeRepository: EmployeeRepository
    @Autowired lateinit var employeeService: EmployeeService
    @Autowired lateinit var cacheManager: RedisCacheManager
    @Autowired lateinit var employeeDAO: EmployeeDAO

    @Before
    fun setUp() {
        employeeDAO.initDB()
    }

    @Test
    fun `should find employee by id`() {
        employeeService.findById(employeeDAO.jntakpe.id!!).test()
                .expectSubscription()
                .consumeNextWith {
                    assertThat(it).isNotNull()
                    assertThat(it.login).isEqualTo(employeeDAO.jntakpe.login)
                }
                .verifyComplete()
    }

    @Test
    fun `should find employee by id 100 times below 2 secs`() {
        val execs = 100
        val duration = Flux.range(0, execs)
                .publishOn(Schedulers.elastic())
                .flatMap { employeeService.findById(employeeDAO.jntakpe.id!!) }.test()
                .expectSubscription()
                .expectNextCount(execs.toLong())
                .verifyComplete()
        assertThat(duration).isLessThan(Duration.ofSeconds(2))
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
        employeeService.findByLogin(employeeDAO.jntakpe.login).test()
                .expectSubscription()
                .consumeNextWith {
                    assertThat(it).isNotNull()
                    assertThat(it.login).isEqualTo(employeeDAO.jntakpe.login)
                }
                .verifyComplete()
    }

    @Test
    fun `should find employee by login ignoring case`() {
        employeeService.findByLogin(employeeDAO.jntakpe.login.toUpperCase()).test()
                .expectSubscription()
                .consumeNextWith { assertThat(it.login).isEqualTo(employeeDAO.jntakpe.login) }
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
        employeeService.findByEmail(employeeDAO.jntakpe.email).test()
                .expectSubscription()
                .consumeNextWith {
                    assertThat(it).isNotNull()
                    assertThat(it.email).isEqualTo(employeeDAO.jntakpe.email)
                }
                .verifyComplete()
    }

    @Test
    fun `should find employee by mail ignoring case`() {
        employeeService.findByEmail(employeeDAO.jntakpe.email.toUpperCase()).test()
                .expectSubscription()
                .consumeNextWith { assertThat(it.email).isEqualTo(employeeDAO.jntakpe.email) }
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
        val updated = employeeDAO.jntakpe.copy(login = "joss")
        employeeService.update(updated, employeeDAO.jntakpe.id!!).test()
                .expectSubscription()
                .consumeNextWith {
                    assertThat(it.login).isEqualTo(updated.login)
                    assertThat(employeeRepository.findByLoginIgnoreCase(updated.login).block()).isNotNull()
                    assertThat(employeeRepository.findByLoginIgnoreCase(employeeDAO.jntakpe.login).block()).isNull()
                }
                .verifyComplete()
    }

    @Test
    fun `should fail to update employee cuz wrong id`() {
        employeeService.update(employeeDAO.jntakpe, ObjectId()).test()
                .expectSubscription()
                .verifyError(IdNotFoundException::class.java)
    }

    @Test
    fun `should update cache when updating employee`() {
        val updated = employeeDAO.jntakpe.copy(login = "cached")
        employeeService.update(updated, employeeDAO.jntakpe.id!!).test()
                .expectSubscription()
                .consumeNextWith {
                    assertThat(it).isNotNull()
                    val cached = cacheManager.getCache("employee_id").get(employeeDAO.jntakpe.id.toString(), Employee::class.java)
                    assertThat(cached).isNotNull()
                    assertThat(cached.login).isEqualTo(updated.login)
                }
                .verifyComplete()
    }

}