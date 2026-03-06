package bss.core.service.account

import bss.core.auth.AccountContext
import bss.core.auth.AccountLoginLog
import bss.core.auth.ExpandAccount
import bss.core.type.ClientPlatform
import org.bson.types.ObjectId
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * 记录登录日志的抽象类
 */
abstract class AbstractLoginLogService<ACCOUNT : ExpandAccount, LOGIN_LOG : AccountLoginLog> {
    abstract fun add(
        user: ACCOUNT, type: String, ua: String?, ip: String?, token: String,
        platform: ClientPlatform,
        sessionId: ObjectId?,
    ): Mono<LOGIN_LOG>

    abstract fun logout(loginLogId: ObjectId, exchange: ServerWebExchange): Mono<LOGIN_LOG>

    abstract fun toAccountContext(json: String): Mono<AccountContext>
}