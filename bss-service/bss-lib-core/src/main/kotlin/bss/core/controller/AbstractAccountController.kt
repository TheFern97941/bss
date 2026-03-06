package bss.core.controller

import bss.core.annotation.NotAuth
import bss.core.auth.AccountLoginLog
import bss.core.auth.ExpandAccount
import bss.core.controller.form.BindGoogleAuthForm
import bss.core.holder.AccountContextHolder
import bss.core.service.account.AbstractAccountService
import bss.core.service.account.form.ChangePwdForm
import bss.core.service.account.form.LoginForm
import bss.core.utils.GoogleAuthenticatorUtils
import jakarta.validation.Valid
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

open class AbstractAccountController<ACCOUNT : ExpandAccount, LOGIN_LOG : AccountLoginLog>(
    private val accountService: AbstractAccountService<ACCOUNT, LOGIN_LOG>,
) {

    /**
     * 登录
     * @return 登录日志信息包含token
     */
    @NotAuth
    @PostMapping("/login")
    open suspend fun login(@Valid @RequestBody loginForm: LoginForm, exchange: ServerWebExchange): LOGIN_LOG {
        return accountService.login(loginForm)
    }

    @NotAuth
    @PostMapping("/logout")
    suspend fun logout() {
        AccountContextHolder.context.awaitSingleOrNull() ?: return
        accountService.logout().awaitSingleOrNull()
    }

    @PostMapping("/pwd")
    fun changePwd(@Valid @RequestBody changePwdForm: ChangePwdForm): Mono<ACCOUNT> {
        return accountService.changePwd(changePwdForm)
    }

    @PostMapping("startGoogleAuth")
    fun startGoogleAuth(): Mono<String> {
        return GoogleAuthenticatorUtils.newKey().toMono()
    }

    @PostMapping("bindMyGoogleAuth")
    fun bindMyGoogleAuth(
        @RequestBody
        form: BindGoogleAuthForm
    ): Mono<Boolean> {
        return accountService.bindAuthKey(form.authKey, form.authCode)
            .thenReturn(true)
    }
}