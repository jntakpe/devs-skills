package com.github.jntakpe.devsskills.extension

import com.github.jntakpe.devsskills.dto.ErrorDTO
import com.github.jntakpe.devsskills.exception.IdNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.web.reactive.function.server.ServerResponse.status
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import reactor.core.publisher.toMono
import javax.validation.ValidationException
import javax.validation.Validator

fun Throwable.toResponse() = when (this) {
    is IdNotFoundException -> NOT_FOUND.toErrorDTO(this.message)
    is ValidationException -> BAD_REQUEST.toErrorDTO(this.message)
    else -> INTERNAL_SERVER_ERROR.toErrorDTO(this.message)
}

fun HttpStatus.toErrorDTO(errMsg: String?) = status(this).syncBody(ErrorDTO(errMsg, this.value(), this.reasonPhrase))

fun <T> Validator.toMono(target: T) = validate(target).toFlux()
        .map { it.message }
        .collectList()
        .doOnNext { print(it) }
        .flatMap { if (it.isEmpty()) Mono.just(target) else ValidationException(it.joinToString()).toMono<T>() }