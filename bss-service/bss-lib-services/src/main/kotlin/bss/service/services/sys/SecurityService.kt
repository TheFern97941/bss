package bss.service.services.sys

import org.springframework.stereotype.Service

@Service
class SecurityService(
    private val sysRoleService: SysRoleService
) {

    fun getModules(role: String): Set<String> {
        val sysRole = sysRoleService.findById(role)
        return sysRole?.moduleId ?: emptySet()
    }

    fun check(role: String, code: String): Boolean {
        sysRoleService.getSysRoleInfo(role)?.let { sysRoleInfo ->
            return sysRoleInfo.moduleMap.containsKey(code)
        }
        return false
    }
}