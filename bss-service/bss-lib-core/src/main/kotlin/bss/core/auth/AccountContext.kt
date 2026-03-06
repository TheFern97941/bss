package bss.core.auth

import org.bson.types.ObjectId
import bss.core.type.Locales
import java.time.LocalDateTime

interface AccountContext {
    /**
     * 登录记录的id
     */
    val loginLogId: ObjectId
    val type: String
    val userId: ObjectId
    val username: String
    val role: String?
    var token: String
    var loginDateTime: LocalDateTime
    val sessionId: ObjectId?
    val locale: Locales

    fun account() = object : Account {
        override val id: ObjectId
            get() = this@AccountContext.userId
        override val role: String?
            get() = this@AccountContext.role
        override val username: String
            get() = this@AccountContext.username
    }
}