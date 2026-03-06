package bss.service.services.sys

import bss.core.auth.AccountContext
import bss.core.auth.ExpandAccount
import bss.core.service.account.AbstractLoginLogService
import bss.core.type.ClientPlatform
import bss.core.type.Locales
import bss.service.entity.sys.LoginLog
import bss.service.repository.sys.LoginLogRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.LocalDateTime


abstract class AbstractBaseLoginLogService<ACCOUNT : ExpandAccount>(
    private val loginLogRepository: LoginLogRepository,
    private val objectMapper: ObjectMapper,
) : AbstractLoginLogService<ACCOUNT, LoginLog>() {
    override fun add(
        user: ACCOUNT, type: String,
        ua: String?, ip: String?,
        token: String,
        platform: ClientPlatform,
        sessionId: ObjectId?,
    ): Mono<LoginLog> {
        val r = LoginLog(
            userId = user.id,
            username = user.username,
            avatar = user.avatar,
            locale = user.locale,
            type = type,
            role = user.role,
            password = user.password,
            clientUa = ua,
            client = ip,
            token = token,
            platform = platform,
            sessionId = sessionId,
        )
        return loginLogRepository.save(r)
    }

    override fun logout(loginLogId: ObjectId, exchange: ServerWebExchange): Mono<LoginLog> {
        return loginLogRepository.findById(loginLogId).map { it.copyOfLogout(exchange) }
            .flatMap(loginLogRepository::save)
    }

    override fun toAccountContext(json: String): Mono<AccountContext> {
        val loginLog = objectMapper.readValue(json, LoginLog::class.java)
        return Mono.just(object : AccountContext {
            override val loginLogId: ObjectId = loginLog.id
            override val type: String = loginLog.type
            override val userId: ObjectId = loginLog.userId
            override val username: String = loginLog.username
            override val role: String?
                get() = loginLog.role
            override var locale: Locales = loginLog.locale
            override var token: String = loginLog.token
            override var loginDateTime: LocalDateTime = loginLog.createdAt
            override val sessionId: ObjectId?
                get() = loginLog.sessionId
        })
    }
}