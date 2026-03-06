package bss.admin.controller.sys

import bss.admin.controller.sys.form.AdminForm
import bss.admin.controller.sys.model.AdminModel
import bss.admin.manager.sys.AdminManager
import bss.core.annotation.RestService
import bss.core.auth.Account
import bss.core.controller.AbstractManagerController
import bss.core.exception.MessageException
import bss.service.entity.sys.Admin
import bss.service.repository.sys.form.UpdateAdminForm
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Mono

@RestService(requestPath = "sysAdmin", securityValue = "sysAdmin")
class SysAdminManagerController(
    val adminManager: AdminManager
): AbstractManagerController<Admin, ObjectId, AdminForm, AdminForm, Admin>(adminManager) {

    /**
     * 解锁账户
     */
    @PostMapping("/unlock/{id}")
    suspend fun unlock(
        @Parameter(hidden = true) account: Account,
        @PathVariable id: ObjectId
    ): Mono<Admin> {
        return adminManager.unlock(id)
    }

    /**
     * 解绑google auth
     */
    @PostMapping("/unbindAuthKey/{id}")
    suspend fun unbindAuthKey(
        @Parameter(hidden = true) account: Account,
        @PathVariable id: ObjectId
    ): Mono<Admin> {
        return adminManager.unbindAuthKey(id)
    }

}