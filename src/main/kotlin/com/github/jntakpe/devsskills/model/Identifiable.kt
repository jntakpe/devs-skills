package com.github.jntakpe.devsskills.model

import org.bson.types.ObjectId
import java.io.Serializable

interface Identifiable : Serializable {

    val id: ObjectId?

}