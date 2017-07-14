package com.github.jntakpe.devsskills.extension

import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.Test

class StringMappingsTest {

    @Test
    fun `should map string to objectId`() {
        val objectId = ObjectId()
        val id = objectId.toString().toId()
        assertThat(id).isNotNull()
        assertThat(id).isEqualTo(objectId)
    }

}