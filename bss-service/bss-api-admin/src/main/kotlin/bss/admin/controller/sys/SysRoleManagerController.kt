package bss.admin.controller.sys

import bss.core.annotation.RestService
import bss.core.controller.AbstractManagerController
import bss.service.entity.sys.SysRole
import bss.service.services.sys.SysRoleService
import bss.service.services.sys.form.SysRoleForm

@RestService(requestPath = "sysRole", securityValue = "sysRole")
class SysRoleManagerController(
    sysRoleService: SysRoleService
): AbstractManagerController<SysRole, String, SysRoleForm, SysRoleForm, SysRole>(sysRoleService)