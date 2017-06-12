package com.github.jntakpe.devsskills.repository

import com.github.jntakpe.devsskills.model.Employee
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface EmployeeRepository : ReactiveMongoRepository<Employee, ObjectId> {

    fun findByEmailIgnoreCase(email: String): Mono<Employee>

}