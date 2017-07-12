package com.github.jntakpe.devsskills.web

import com.github.jntakpe.devsskills.extension.json
import com.github.jntakpe.devsskills.extension.toDTO
import com.github.jntakpe.devsskills.extension.toResponse
import com.github.jntakpe.devsskills.service.EmployeeService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.toMono
import javax.validation.ValidationException

@Component
class EmployeeHandler(private val employeeService: EmployeeService) {

    fun findByLogin(req: ServerRequest) = employeeService.findByLogin(loginFromPath(req))
            .map { it.toDTO() }
            .switchIfEmpty(ValidationException("Employee with login ${loginFromPath(req)} not found").toMono())
            .flatMap { ServerResponse.ok().json().syncBody(it) }
            .onErrorResume { it.toResponse() }

    private fun loginFromPath(req: ServerRequest) = req.pathVariable("login")

}