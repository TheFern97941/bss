package bss.service.services.sys

import bss.core.service.AbstractCacheManagerService
import bss.core.utils.DataClassUtils
import bss.service.entity.sys.SysRole
import bss.service.repository.sys.SysRoleRepository
import bss.service.services.sys.form.SysRoleForm
import bss.service.services.sys.model.SysRoleInfo
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.ConcurrentHashMap

@Service
class SysRoleService(repository: SysRoleRepository) :
    AbstractCacheManagerService<SysRole, String, SysRoleForm, SysRoleForm, SysRole>(repository) {


    @Autowired
    private lateinit var sysModuleService: SysModuleService

    private val roleInfoMap = ConcurrentHashMap<String, SysRoleInfo>()

    @PostConstruct
    override fun init() {
        sysModuleService.listenOnInit {
            super.init()
        }
    }

    override fun onInit(list: List<SysRole>) {
        sysModuleService.listen { sysModule ->
            roleInfoMap.forEach { (_, sysRoleInfo) ->
                sysRoleInfo.moduleMap.computeIfPresent(sysRoleInfo.id) { _, _ -> sysModule }
            }
        }
        sysModuleService.listenDelete { sysModuleId ->
            roleInfoMap.forEach { (_, sysRoleInfo) ->
                sysRoleInfo.moduleMap.remove(sysModuleId)
            }
        }
    }

    fun getSysRoleInfo(id: String): SysRoleInfo? = roleInfoMap[id]

    override fun SysRole.getId(): String {
        return this.id;
    }

    override fun onDelCache(t: SysRole) {
        super.onDelCache(t)
        roleInfoMap.remove(t.id)
    }

    //    override fun onDelCache(sysRole: SysRole) = roleInfoMap.remove(sysRole.id)

    override fun onCache(t: SysRole, replace: Boolean) {
        roleInfoMap[t.id] = to(t)
    }

    private fun to(sysRole: SysRole): SysRoleInfo {
        val map = sysRole.moduleId
            .mapNotNull(sysModuleService::findById)
            .associateByTo(ConcurrentHashMap()) { it.id }

        return SysRoleInfo(sysRole.id, sysRole.name, map)
    }

    override fun handleCreate(form: SysRoleForm, vararg pairs: Pair<String, Any>): Mono<SysRole> {
        return DataClassUtils.new(cls, form, *pairs,SysRole::moduleId.name to HashSet(form.moduleId)).toMono()
    }

    override fun handleUpdate(
        id: String,
        form: SysRoleForm,
        t: SysRole,
        vararg pairs: Pair<String, Any>
    ): Mono<SysRole> {
        return  DataClassUtils.new(cls, form, *pairs,SysRole::moduleId.name to HashSet(form.moduleId)).toMono()
    }
}