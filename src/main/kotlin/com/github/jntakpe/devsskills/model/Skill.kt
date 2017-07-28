package com.github.jntakpe.devsskills.model

import com.github.jntakpe.devsskills.config.NoArg
import org.bson.types.ObjectId
import java.math.BigDecimal
import java.util.*

@NoArg
data class Skill(val category: SkillCategory,
                 val name: String,
                 val ratings: List<Rating> = emptyList(),
                 val mean: BigDecimal = BigDecimal.ZERO,
                 override val id: ObjectId = ObjectId()) : Identifiable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Skill) return false
        return Objects.equals(category, other.category) && Objects.equals(name, other.name)
    }

    override fun hashCode(): Int {
        return Objects.hash(category, name)
    }

    override fun toString(): String {
        return "Skill(category=$category, name='$name', mean=$mean)"
    }
}
