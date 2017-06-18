package com.github.jntakpe.devsskills.service

import com.github.jntakpe.devsskills.model.Employee
import com.github.jntakpe.devsskills.model.Skill
import com.github.jntakpe.devsskills.model.SkillCategory.BACKEND
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.test

@SpringBootTest
@RunWith(SpringRunner::class)
class CacheServiceTest {

    @Autowired lateinit var cacheService: CacheService
    @Autowired lateinit var cacheManager: RedisCacheManager
    @Autowired lateinit var redisConnectionFactory: RedisConnectionFactory
    val jntakpe = Employee("jntakpe", "jntakpe@mail.com", "Jocelyn", "NTAKPE", setOf(Skill(BACKEND, "Java")), ObjectId())

    @Before
    fun setUp() {
        redisConnectionFactory.connection.flushDb()
    }

    @Test
    fun `should store id in cache`() {
        val cache = cacheManager.getCache("employee_id")
        assertThat(cache.get(jntakpe.id.toString())).isNull()
        cacheService.store(jntakpe, Employee::id)
        assertThat(cache.get(jntakpe.id.toString())).isNotNull()
    }

    @Test
    fun `should store email in cache`() {
        val cache = cacheManager.getCache("employee_email")
        assertThat(cache.get(jntakpe.email)).isNull()
        cacheService.store(jntakpe, Employee::email, jntakpe.email)
        assertThat(cache.get(jntakpe.email)).isNotNull()
    }

    @Test
    fun `should retrieve value from cache`() {
        cacheManager.getCache("employee_id").put(jntakpe.id.toString(), jntakpe)
        cacheService.retrieve(jntakpe.id.toString(), Employee::class.java, Employee::id).test()
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete()
    }

    @Test
    fun `should not retrieve value from cache`() {
        cacheService.retrieve(jntakpe.id.toString(), Employee::class.java, Employee::id).test()
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete()
    }

}