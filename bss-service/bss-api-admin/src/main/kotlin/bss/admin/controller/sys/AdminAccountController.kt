package bss.admin.controller.sys

import bss.service.repository.sys.form.UpdateAdminForm
import bss.admin.controller.sys.model.AdminModel
import bss.core.annotation.RestService
import bss.core.holder.AccountContextHolder
import bss.core.controller.AbstractAccountController
import bss.core.exception.MessageException
import bss.core.utils.DataClassUtils
import bss.service.entity.sys.Admin
import bss.service.entity.sys.LoginLog
import bss.admin.manager.sys.AdminAccountManager
import bss.core.auth.Account
import bss.service.services.sys.SecurityService
import org.apache.commons.lang3.StringUtils
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Mono


/**
 * 登录接口
 */
@RestService(
    requestPath = "account",
    securityValue = "account"
)
class AdminAccountController(
    val adminAccountManager: AdminAccountManager,
    val securityService: SecurityService,
) : AbstractAccountController<Admin, LoginLog>(adminAccountManager) {
    /**
     * 查询当前登录的管理员信息
     */
    @GetMapping()
    suspend fun getCurrent(): AdminModel {
        val account = AccountContextHolder.context.awaitSingle().account()
        if (account.role == null) {
            throw MessageException("role.notFind", "not found role")
        }
        val modules = securityService.getModules(account.role!!)
        val admin = adminAccountManager.findById(account.id).awaitSingle()
        return toModel(admin, modules)
    }

    /**
     * 查询当前登录的管理员信息
     */
    @PostMapping()
    suspend fun updateSelf(
        @Parameter(hidden = true) account: Account,
        @RequestBody @Valid body: UpdateAdminForm
    ): Mono<AdminModel> {

        if (account.role == null) {
            throw MessageException("role.notFind", "not found role")
        }
        val modules = securityService.getModules(account.role!!)
        return adminAccountManager.updateSelf(body)
            .map {
                toModel(it, modules)
            }
    }

    private fun toModel(admin: Admin, modules: Set<String>): AdminModel {
        return DataClassUtils.new(
            AdminModel::class,
            admin,
            Pair(AdminModel::bindAuthKey.name, StringUtils.isNotBlank(admin.authKey)),
            Pair(AdminModel::modules.name, modules)
        )
    }
}