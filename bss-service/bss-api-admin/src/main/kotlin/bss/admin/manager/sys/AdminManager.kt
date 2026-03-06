package bss.admin.manager.sys

import bss.core.exception.MessageException
import bss.core.service.AbstractManagerService
import bss.core.service.account.AbstractAccountService
import bss.service.entity.sys.Admin
import bss.admin.controller.sys.form.AdminForm
import bss.core.service.account.AbstractAccountService.Companion.encodePwd
import bss.core.utils.DataClassUtils
import bss.service.entity.sys.LoginLog
import bss.service.repository.sys.AdminRepository
import bss.service.repository.sys.form.UpdateAdminForm
import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

private val log = KotlinLogging.logger {}

@Service
class AdminManager(
    val adminRepository: AdminRepository,
) : AbstractManagerService<Admin, ObjectId, AdminForm, AdminForm, Admin>(adminRepository) {

    @PostConstruct
    fun init() {
        adminRepository.findByUsername("test")
            .switchIfEmpty { create(AdminForm("test", "test")) }
            .subscribe {
                log.info { "result:${it}" }
            }
    }


    fun onLoginError(account: Admin?, e: Throwable) {
        if (account != null) {
            adminRepository.incrLoginError(account.id).subscribe()
        }
    }

    fun onLoginSuccess(loginLog: LoginLog) = adminRepository.unlock(loginLog.userId).subscribe()

    fun findByUsername(name: String) = adminRepository.findByUsername(name)

    fun save(user: Admin) = adminRepository.save(user)

    override fun handleUpdate(id: ObjectId, form: AdminForm, t: Admin, vararg pairs: Pair<String, Any>): Mono<Admin> {
        if (!form.password.isNullOrBlank()) {
            val pwd = encodePwd(t.id, form.password)
            return super.handleUpdate(id, form, t, Admin::password.name to pwd)
        }
        return super.handleUpdate(id, form, t, Admin::password.name to t.password)
    }

    override fun handleCreate(form: AdminForm, vararg pairs: Pair<String, Any>): Mono<Admin> {
        if (!form.password.isNullOrBlank()) {
            val id = ObjectId.get()
            val pwd = encodePwd(id, form.password)
            return super.handleCreate(form, Admin::password.name to pwd, Admin::id.name to id)
        }
        return super.handleCreate(form, *pairs)
    }

    override fun handleDuplicateKeyError(it: Throwable, t: Admin): Mono<Admin> {
        return MessageException("name.duplicate", "用户名重复", it).toMono()
    }



    fun unlock(id: ObjectId): Mono<Admin> {
        return adminRepository.unlock(id)
    }

    fun savePwd(id: ObjectId, encodePwd: String) = adminRepository.savePwd(id, encodePwd)

    fun saveAuthKey(id: ObjectId, authKey: String) = adminRepository.saveAuthKey(id, authKey)
    fun unbindAuthKey(id: ObjectId) = adminRepository.unbindAuthKey(id)

    fun updateInfo(id: ObjectId, body: UpdateAdminForm) = adminRepository.updateInfo(id, body)
}