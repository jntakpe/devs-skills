package com.github.jntakpe.devsskills.service

import com.github.jntakpe.devsskills.model.Employee
import org.slf4j.LoggerFactory
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class EmployeeCacheService(private val cacheManager: RedisCacheManager) {

    val logger = LoggerFactory.getLogger(javaClass.simpleName)
    val className = Employee::class.simpleName

    fun store(employee: Employee): Employee {
        logger.info("Storing {} in cache", employee)
        val cache = cacheManager.getCache("${className}_${Employee::id.name}")
        cache.put(employee.id.toString(), employee)
        return employee
    }

    fun retrieve(propertyName: String, value: String): Mono<Employee> {
        logger.debug("Retrieving employee with $propertyName and value : $value")
        val cache = cacheManager.getCache("${className}_$propertyName")
        val employee = cache.get(value, Employee::class.java)
        if (employee == null) {
            logger.debug("Cache miss from cache {} with key {}", cache.name, value)
            return Mono.empty()
        } else {
            logger.debug("{} retrieved from cache {} with key {}", employee, cache.name, value)
            return Mono.just(employee)
        }
    }
}