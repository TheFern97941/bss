package bss.core.auth

import bss.core.type.Locales
import org.bson.types.ObjectId

interface Account {
    val id: ObjectId
    val role: String?
    val username: String
}

interface ExpandAccount : Account {
    val avatar: String?
    val lock: Int
    val password: String
    val locale: Locales
    val authKey: String?
}