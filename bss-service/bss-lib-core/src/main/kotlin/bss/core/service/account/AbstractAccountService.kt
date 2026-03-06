package bss.core.service.account

import bss.core.auth.*
import bss.core.exception.MessageException.Companion.COMMON_AUTH_CODE_NOT_MATCH
import bss.core.exception.MessageException.Companion.COMMON_LOGIN_LOCKED
import bss.core.exception.MessageException.Companion.COMMON_LOGIN_NOT_FOUND
import bss.core.exception.MessageException.Companion.COMMON_LOGIN_TIMEOUT
import bss.core.exception.MessageException.Companion.COMMON_PWD_NOT_MATCH
import bss.core.extensions.getServerWebExchange
import bss.core.holder.AccountContextHolder
import bss.core.properties.CoreProperties
import bss.core.service.account.form.ChangePwdForm
import bss.core.service.account.form.LoginForm
import bss.core.type.ClientPlatform
import bss.core.utils.GoogleAuthenticatorUtils
import bss.core.utils.IpUtils
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.Resource
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.StringUtils
import org.bson.types.ObjectId
import org.springframework.data.redis.core.ReactiveListOperations
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import java.time.Duration
import java.util.concurrent.atomic.AtomicReference

abstract class AbstractAccountService<ACCOUNT : ExpandAccount, LOGIN_LOG : AccountLoginLog>(
    protected var type: String,
    private val loginLogService: AbstractLoginLogService<ACCOUNT, LOGIN_LOG>,
) : AccountManager {

    @Resource
    lateinit var objectMapper: ObjectMapper

    @Resource
    lateinit var reactiveStringRedisTemplate: ReactiveStringRedisTemplate

    @Resource
    lateinit var coreProperties: CoreProperties

    private val auth get() = coreProperties.auth

    private val listOps get() = reactiveStringRedisTemplate.opsForList()

    private val valueOps get() = reactiveStringRedisTemplate.opsForValue()

    abstract fun onLoginError(account: ACCOUNT?, e: Throwable)

    abstract fun onLoginSuccess(loginLog: LOGIN_LOG)

    abstract fun findByAccount(account: String): Mono<ACCOUNT>

    abstract fun findById(id: ObjectId): Mono<ACCOUNT>

    abstract fun savePwd(id: ObjectId, encodePwd: String): Mono<ACCOUNT>

    abstract fun saveAuthKey(id: ObjectId, authKey: String): Mono<ACCOUNT>

    fun bindAuthKey(authKey: String, authCode: Int): Mono<ACCOUNT> {
        return AccountContextHolder.context.flatMap { acHolder ->
            if (!GoogleAuthenticatorUtils.authorize(authKey, authCode)) {
                throw COMMON_AUTH_CODE_NOT_MATCH
            }
            saveAuthKey(acHolder.userId, authKey)
        }
    }

    fun changePwd(form: ChangePwdForm): Mono<ACCOUNT> {
        return AccountContextHolder.context.flatMap { acHolder ->
            val encodeOldPwd = encodePwd(acHolder.userId, form.oldPassword)
            val encodePwd = encodePwd(acHolder.userId, form.password)
            findById(acHolder.userId).filter { it.password == encodeOldPwd }
                .switchIfEmpty { COMMON_PWD_NOT_MATCH.toMono() }
                .flatMap { this.savePwd(it.id, encodePwd) }
        }
    }

    fun logout(): Mono<Void> {
        val exchangeRef = AtomicReference<ServerWebExchange>()
        return Mono.deferContextual(::getServerWebExchange).doOnNext(exchangeRef::set)
            .flatMap { AccountContextHolder.context }
            .flatMap { loginLogService.logout(it.loginLogId, exchangeRef.get()) }
            .flatMap { a -> clearByToken(a.token, a.userId) }.then()
    }

    private fun clearByToken(token: String, accountId: ObjectId): Mono<Long> {
        return reactiveStringRedisTemplate.delete(getKey(token)).publishOn(Schedulers.boundedElastic())
            .doOnSuccess { clearTokenListItem(accountId, token).subscribe() }
    }

    open suspend fun login(form: LoginForm, timeOutHours: Long = auth.timeOutHours): LOGIN_LOG {
        var account: ACCOUNT? = null
        try {
            // 这里可以考虑先去redis中 拉一次是否已经冻结了!
            // 毕竟是MongoDB, 对于速度, 应该差不多, 而且就算redis 拿到了状态, 还要拿数据库中的所有数据进行判断!
            account = findByAccount(form.account).awaitSingleOrNull()
                ?: throw COMMON_LOGIN_NOT_FOUND

            if (StringUtils.isNotEmpty(account.authKey) &&
                (form.authCode?.toIntOrNull() == null
                        || !GoogleAuthenticatorUtils.authorize(account.authKey!!, form.authCode!!.toInt()))
            ) {
                throw COMMON_AUTH_CODE_NOT_MATCH
            }

            val authLoginTokenMax = validPassword(account, form.password)

            return doLogin(account, authLoginTokenMax, timeOutHours).awaitSingle()
        } catch (e: Throwable) {
            this.onLoginError(account, e)
            throw e
        }
    }

    protected open fun validPassword(account: ACCOUNT, pwd: String): Int {
        if (account.password != encodePwd(account.id, pwd)) {
            throw COMMON_LOGIN_NOT_FOUND
        }
        return auth.authLoginTokenMax
    }

    protected open fun doLogin(
        account: ACCOUNT,
        authLoginTokenMax: Int,
        timeOutHours: Long = auth.timeOutHours,
    ): Mono<LOGIN_LOG> {
        return saveLog(account)
            .flatMap { saveAllLoginInfo(it, account, authLoginTokenMax, timeOutHours) }
            .doOnNext { onLoginSuccess(it) }
            .map { doTransform(it, account) }
    }

    protected open fun doTransform(loginLog: LOGIN_LOG, account: ACCOUNT): LOGIN_LOG {
        return loginLog;
    }


    private fun saveLog(account: ACCOUNT): Mono<LOGIN_LOG> {

        if (account.lock >= auth.authLoginErrorMax) {
            throw COMMON_LOGIN_LOCKED
        }

        return Mono
            .deferContextual(::getServerWebExchange)
            .flatMap { exchange ->
                val ip = IpUtils.getIp(exchange)
                val ua = exchange.request.headers.getFirst("user-agent")
                val token = RandomStringUtils.randomAlphanumeric(32)
                loginLogService.add(account, type, ua, ip, token, ClientPlatform.web, null)
            }
    }

    override fun auth(token: String): Mono<AccountContext> {
        return authNotError(token)
            .switchIfEmpty { COMMON_LOGIN_TIMEOUT.toMono() }
    }

    fun authNotError(token: String): Mono<AccountContext> {
        val key = getKey(token)
        return valueOps[key]
            .doOnNext { doTimeOutDelayMinutes(key) }
            .flatMap { loginLogService.toAccountContext(it) }
    }

    private fun doTimeOutDelayMinutes(key: String) {
        if (auth.timeOutDelayMinutes > 0) {
            reactiveStringRedisTemplate.expire(key, Duration.ofMinutes(auth.timeOutDelayMinutes)).subscribe()
        }
    }

    open fun saveTokenList(account: ACCOUNT, token: String, authLoginTokenMax: Int): Mono<Long> {
        val tokenListKey: String = getTokenListKey(account.id)

        return listOps.leftPush(tokenListKey, token).flatMap {
            when {
                (it > authLoginTokenMax) -> clearTokenListExceed(tokenListKey, account, it)
                else -> Mono.just(it)
            }
        }
    }

    private fun clearTokenListExceed(tokenListKey: String, account: ACCOUNT, size: Long): Mono<Long> {
        return listOps.rightPop(tokenListKey).flatMap { clearByToken(it, account.id) }.thenReturn(size)
    }

    fun clearLogin(accountId: ObjectId): Mono<Long> {
        val opt: ReactiveListOperations<String, String> = reactiveStringRedisTemplate.opsForList()
        val tokenListKey = getTokenListKey(accountId)
        return opt.range(tokenListKey, 0, -1).flatMap { clearByToken(it, accountId) }.count()
    }

    fun clearAllLogin(): Mono<Long> {
        val key = "admin_" + type.lowercase() + "_" + "*"
        return reactiveStringRedisTemplate.delete(reactiveStringRedisTemplate.keys(key))
    }

    open fun saveAllLoginInfo(
        loginRecord: LOGIN_LOG,
        account: ACCOUNT,
        authLoginTokenMax: Int,
        timeOutHours: Long = auth.timeOutHours,
    ): Mono<LOGIN_LOG> {
        val token = loginRecord.token
        val json: String = objectMapper.writeValueAsString(loginRecord)
        val key = getKey(token)
        val user = valueOps.set(key, json, Duration.ofHours(timeOutHours))
        return Mono.zip(saveTokenList(account, token, authLoginTokenMax), user)
            .thenReturn(loginRecord)
    }


    private fun clearTokenListItem(accountId: ObjectId, token: String): Mono<Long> {
        val opt: ReactiveListOperations<String, String> = reactiveStringRedisTemplate.opsForList()
        val tokenListKey = getTokenListKey(accountId)
        return opt.remove(tokenListKey, 0, token)
    }

    private fun getTokenListKey(adminId: ObjectId): String = "tokenList:" + type.lowercase() + ":" + adminId

    private fun getKey(token: String): String = "loginLog:" + type.lowercase() + ":" + token


    companion object {
        fun encodePwd(id: ObjectId, pwd: String): String = DigestUtils.sha256Hex(pwd + id.toHexString())

        fun encodePwd(uid: Int, pwd: String): String = DigestUtils.sha256Hex(pwd + uid)
    }
}