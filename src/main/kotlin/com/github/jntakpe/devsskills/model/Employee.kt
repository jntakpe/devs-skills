package com.github.jntakpe.devsskills.model

import com.github.jntakpe.devsskills.config.NoArg
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@NoArg
@Document
data class Employee(val email: String,
                    val firstName: String,
                    val lastName: String,
                    val skills: List<Skill> = emptyList(),
                    val id: ObjectId? = null) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Employee) return false
        return email == other.email
    }

    override fun hashCode(): Int {
        return email.hashCode()
    }

    override fun toString(): String {
        return "Employee (id:$id, email=$email, firstName=$firstName, lastName=$lastName)"
    }

}