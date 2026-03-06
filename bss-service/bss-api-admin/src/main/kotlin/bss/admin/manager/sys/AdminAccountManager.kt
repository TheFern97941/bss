package bss.admin.manager.sys

import bss.admin.controller.sys.form.AdminForm
import bss.admin.controller.sys.form.AdminLoginForm
import bss.service.repository.sys.form.UpdateAdminForm
import bss.core.exception.MessageException
import bss.core.exception.MessageException.Companion.COMMON_LOGIN_LOCKED
import bss.core.exception.MessageException.Companion.COMMON_LOGIN_NOT_FOUND
import bss.core.holder.AccountContextHolder
import bss.core.service.account.AbstractAccountService
import bss.service.entity.sys.Admin
import bss.service.entity.sys.LoginLog
import bss.service.enums.StatusEnums
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

private val log = KotlinLogging.logger {}

@Service
class AdminAccountManager(
    private val adminManager: AdminManager,
    adminLoginLogManager: AdminLoginLogManager,
) : AbstractAccountService<Admin, LoginLog>(
    "admin", adminLoginLogManager,
) {

    @OptIn(DelicateCoroutinesApi::class)
    @PostConstruct
    fun init(){
        GlobalScope.launch {
            val admin = adminManager.findByUsername("admin").awaitSingleOrNull()
            if (admin == null) {
                adminManager.create(AdminForm("admin", "admin")).awaitSingle()
            }
        }
    }

    suspend fun loginByType(form: AdminLoginForm): LoginLog {
        var account: Admin? = null
        try {
            account = findByAccount(form.account)
                .doOnNext {
                    log.info { "account:${it}" }
                }
                .awaitSingleOrNull()
                ?: throw MessageException("login.notFind", "未找到用户")
            val authLoginTokenMax = validPassword(account, form.password)
            return doLogin(account, authLoginTokenMax).awaitSingle()
        } catch (e: Throwable) {
            this.onLoginError(account, e)
            throw e
        }
    }


    override fun onLoginError(account: Admin?, e: Throwable) {
        adminManager.onLoginError(account, e)
    }

    override fun onLoginSuccess(loginLog: LoginLog) {
        adminManager.onLoginSuccess(loginLog)
    }

    override fun findByAccount(account: String): Mono<Admin> {
        return adminManager.findByUsername(account)
            .switchIfEmpty { COMMON_LOGIN_NOT_FOUND.toMono() }
            .filter { StatusEnums.NORMAL == it.status }
            .switchIfEmpty { COMMON_LOGIN_LOCKED.toMono() }
    }

    override fun findById(id: ObjectId): Mono<Admin> = adminManager.findByIdToModel(id)
        .ofType(Admin::class.java)

    override fun savePwd(id: ObjectId, encodePwd: String) = adminManager.savePwd(id, encodePwd)

    override fun saveAuthKey(id: ObjectId, authKey: String) = adminManager.saveAuthKey(id, authKey)

    suspend fun updateSelf(body: UpdateAdminForm): Mono<Admin> {
        val account = AccountContextHolder.context.awaitSingle().account()
        return adminManager.updateInfo(account.id, body)
    }
}