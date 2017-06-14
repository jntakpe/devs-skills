package com.github.jntakpe.devsskills.model

import com.github.jntakpe.devsskills.config.NoArg
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@NoArg
@Document
data class Employee(@field:Id val login: String,
                    val email: String,
                    val firstName: String,
                    val lastName: String,
                    val skills: List<Skill> = emptyList()) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Employee) return false
        return login == other.login
    }

    override fun hashCode(): Int {
        return login.hashCode()
    }

    override fun toString(): String {
        return "Employee (login:$login, email=$email, firstName=$firstName, lastName=$lastName)"
    }

}