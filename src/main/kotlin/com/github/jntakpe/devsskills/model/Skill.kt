package com.github.jntakpe.devsskills.model

import com.github.jntakpe.devsskills.config.NoArg
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

@NoArg
data class Skill(val category: SkillCategory,
                 val name: String,
                 val votes: List<Vote> = emptyList(),
                 val mean: BigDecimal = BigDecimal.ZERO) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Skill) return false
        return Objects.equals(category, other.category) && Objects.equals(name, other.name)
    }

    override fun hashCode(): Int {
        return Objects.hash(category, name)
    }

    override fun toString(): String {
        return super.toString()
    }
}
