package com.github.jntakpe.devsskills.dto

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank
import javax.validation.constraints.Size

data class EmployeeDTO(
        @field:NotBlank val id: String,
        @field:NotBlank @field:Size(min = 2) val login: String,
        @field:NotBlank @field:Email val email: String,
        @field:NotBlank val firstName: String,
        @field:NotBlank val lastName: String
)