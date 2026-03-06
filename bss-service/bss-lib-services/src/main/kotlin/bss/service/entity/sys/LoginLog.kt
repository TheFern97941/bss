package bss.service.entity.sys

import bss.core.auth.AccountLoginLog
import bss.core.type.ClientPlatform
import bss.core.type.Locales
import bss.core.utils.IpUtils
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.web.server.ServerWebExchange
import java.time.LocalDateTime

@Document
data class LoginLog(
    @Id
    override val id: ObjectId = ObjectId.get(),

    override val userId: ObjectId,
    val username: String,
    val type: String,

    @Indexed
    val sessionId: ObjectId? = null,
    val role: String? = null,
    val avatar: String?,
    val locale: Locales = Locales.zh_CN,

    var password: String?,

    val clientUa: String?,
    val client: String?,

    val platform: ClientPlatform,

    override val token: String,

    val logoutClientUa: String? = null,
    val logoutClient: String? = null,
    val logoutDate: LocalDateTime? = null,

    var isMyUser: Boolean = false,
    var isAgent: Boolean = false,

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    @Version
    val version: Long = 0,
) : AccountLoginLog {
    fun copyOfLogout(
        exchange: ServerWebExchange,
    ) = this.copy(
        logoutDate = LocalDateTime.now(),
        logoutClientUa = exchange.request.headers.getFirst("user-agent"),
        logoutClient = IpUtils.getIp(exchange),
    )
}