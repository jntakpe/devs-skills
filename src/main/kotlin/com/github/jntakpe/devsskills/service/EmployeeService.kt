package com.github.jntakpe.devsskills.service

import com.github.jntakpe.devsskills.exception.IdNotFoundException
import com.github.jntakpe.devsskills.model.Employee
import com.github.jntakpe.devsskills.repository.EmployeeRepository
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@Service
class EmployeeService(private val employeeRepository: EmployeeRepository, private val employeeCacheService: EmployeeCacheService) {

    val logger = LoggerFactory.getLogger(javaClass.simpleName)

    fun findById(id: ObjectId): Mono<Employee> {
        logger.debug("Searching employee with id {}", id)
        return Flux.concat(employeeCacheService.retrieve(Employee::id.name, id.toString()),
                employeeRepository.findById(id).map { employeeCacheService.store(it) })
                .take(1)
                .singleOrEmpty()
                .doOnNext { logger.debug("{} found using id", it) }
    }

    fun findByLogin(login: String): Mono<Employee> {
        logger.debug("Searching employee with login {}", login)
        return employeeRepository.findByLoginIgnoreCase(login)
                .doOnNext { logger.debug("{} found using login", it) }
    }

    fun findByEmail(email: String): Mono<Employee> {
        logger.debug("Searching employee with email {}", email)
        return employeeRepository.findByEmailIgnoreCase(email)
                .doOnNext { logger.debug("{} found using email", it) }
    }

    fun update(employee: Employee, id: ObjectId): Mono<Employee> {
        return findById(id)
                .switchIfEmpty(IdNotFoundException("Id $id not found").toMono())
                .flatMap { save(employee) }
    }

    private fun save(employee: Employee): Mono<Employee> {
        logger.info("Saving employee {}", employee)
        return employeeRepository.save(employee)
                .doOnNext { logger.info("{} saved", it) }
    }

}