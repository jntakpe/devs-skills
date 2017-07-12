package com.github.jntakpe.devsskills.extension

import com.github.jntakpe.devsskills.exception.IdNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.validator.constraints.NotBlank
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.test
import java.lang.IllegalStateException
import javax.validation.ValidationException
import javax.validation.Validator

@SpringBootTest
@RunWith(SpringRunner::class)
class ErrorMappingsTest {

    @Autowired lateinit var validator: Validator

    @Test
    fun `should map IdNotFoundException to not found response`() {
        val response = IdNotFoundException("Id x not found").toResponse()
        response.test()
                .expectSubscription()
                .consumeNextWith {
                    assertThat(it).isNotNull()
                    assertThat(it.statusCode().is4xxClientError).isTrue()
                    assertThat(it.statusCode()).isEqualTo(HttpStatus.NOT_FOUND)
                }
                .verifyComplete()
    }

    @Test
    fun `should map ValidationException to bad request response`() {
        val response = ValidationException("Invalid field x").toResponse()
        response.test()
                .expectSubscription()
                .consumeNextWith {
                    assertThat(it).isNotNull()
                    assertThat(it.statusCode().is4xxClientError).isTrue()
                    assertThat(it.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
                }
                .verifyComplete()
    }

    @Test
    fun `should map unexpected exception to internal server error response`() {
        val response = IllegalStateException("Oops").toResponse()
        response.test()
                .expectSubscription()
                .consumeNextWith {
                    assertThat(it).isNotNull()
                    assertThat(it.statusCode().is5xxServerError).isTrue()
                    assertThat(it.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                }
                .verifyComplete()
    }


    @Test
    fun `should fail validation and throw an ValidationException`() {
        validator.validateToMono(ToValidate("", "")).test()
                .expectSubscription()
                .verifyError(ValidationException::class.java)
    }

    @Test
    fun `should succeed validation`() {
        val target = ToValidate("Test", "")
        validator.validateToMono(target).test()
                .expectSubscription()
                .expectNext(target)
                .verifyComplete()
    }

}

data class ToValidate(@field:NotBlank val msg: String, val label: String)