package com.github.jntakpe.devsskills.model

import com.github.jntakpe.devsskills.config.NoArg
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@NoArg
@Document
data class Employee(val login: String,
                    val email: String,
                    val firstName: String,
                    val lastName: String,
                    val skills: Set<Skill> = emptySet(),
                    val id: ObjectId? = null) {

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