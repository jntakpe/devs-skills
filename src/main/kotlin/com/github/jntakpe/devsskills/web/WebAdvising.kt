package com.github.jntakpe.devsskills.web

import com.github.jntakpe.devsskills.dto.ErrorDTO
import com.github.jntakpe.devsskills.exception.IdNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.Exception
import java.security.SecureRandom
import javax.validation.ValidationException

@ControllerAdvice
class WebAdvising {

    val logger = LoggerFactory.getLogger(javaClass.simpleName)

    val random = SecureRandom()

    @ExceptionHandler(IdNotFoundException::class)
    fun handleNotFound(exception: IdNotFoundException) = ResponseEntity(ErrorDTO(exception.message, NOT_FOUND.reasonPhrase), NOT_FOUND)

    @ExceptionHandler(ValidationException::class)
    fun handleValidation(exception: ValidationException) = ResponseEntity(ErrorDTO(exception.message, BAD_REQUEST.reasonPhrase), BAD_REQUEST)

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception) = ResponseEntity(ErrorDTO(exception.message, INTERNAL_SERVER_ERROR.reasonPhrase), INTERNAL_SERVER_ERROR)

}