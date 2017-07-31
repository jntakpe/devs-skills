package com.github.jntakpe.devsskills.extension

import org.bson.types.ObjectId

fun String.toId() = ObjectId(this)