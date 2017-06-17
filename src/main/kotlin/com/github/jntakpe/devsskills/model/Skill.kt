package com.github.jntakpe.devsskills.model

import com.github.jntakpe.devsskills.config.NoArg
import java.io.Serializable
import java.math.BigDecimal

@NoArg
data class Skill(val category: SkillCategory,
                 val name: String,
                 val votes: List<Vote> = emptyList(),
                 val mean: BigDecimal = BigDecimal.ZERO) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Skill) return false
        return name.toLowerCase() == other.name.toLowerCase()
    }

    override fun hashCode(): Int {
        return name.toLowerCase().hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}
