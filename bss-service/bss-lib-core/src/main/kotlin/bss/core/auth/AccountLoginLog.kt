package bss.core.auth

import org.bson.types.ObjectId

interface AccountLoginLog {
    val token: String
    val userId: ObjectId
    val id: ObjectId
}