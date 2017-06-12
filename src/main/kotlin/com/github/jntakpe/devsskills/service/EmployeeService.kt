package com.github.jntakpe.devsskills.service

import com.github.jntakpe.devsskills.model.Employee
import com.github.jntakpe.devsskills.repository.EmployeeRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class EmployeeService(private val employeeRepository: EmployeeRepository) {

    val logger = LoggerFactory.getLogger(javaClass.simpleName)

    fun findByEmail(email: String): Mono<Employee> {
        logger.debug("Searching employee with email {}", email)
        return employeeRepository.findByEmailIgnoreCase(email)
                .doOnNext { logger.debug("{} found using email", it) }
    }

}