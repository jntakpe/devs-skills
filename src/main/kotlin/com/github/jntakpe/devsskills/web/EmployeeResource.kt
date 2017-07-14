package com.github.jntakpe.devsskills.web

import com.github.jntakpe.devsskills.extension.toDTO
import com.github.jntakpe.devsskills.service.EmployeeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.toMono
import javax.validation.ValidationException

@RestController
@RequestMapping(Urls.EMPLOYEES_API)
class EmployeeResource(private val employeeService: EmployeeService) {

    @GetMapping("/login/{login}")
    fun findByLogin(@PathVariable login: String) = employeeService.findByLogin(login)
            .map { it.toDTO() }
            .switchIfEmpty(ValidationException("Employee with login $login not found").toMono())

}