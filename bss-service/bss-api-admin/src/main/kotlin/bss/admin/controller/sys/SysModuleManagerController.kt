package bss.admin.controller.sys

import bss.core.annotation.RestService
import bss.core.annotation.SecurityAnnotation
import bss.core.controller.AbstractManagerController
import bss.service.entity.sys.SysModule
import bss.service.services.sys.SysModuleService
import bss.service.services.sys.form.SysModuleForm
import bss.service.services.sys.model.SysModuleModel
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.PostMapping
import reactor.core.publisher.Mono

@RestService(requestPath = "sysModule", securityValue = "sysModule")
class SysModuleManagerController(
    val sysModuleService: SysModuleService
): AbstractManagerController<SysModule, String, SysModuleForm, SysModuleForm, SysModuleModel>(sysModuleService) {

    @SecurityAnnotation("*.show")
    @PostMapping("tree")
    open fun findTree(): Mono<Page<SysModuleModel>> {
        return sysModuleService.findTree()
    }

    @SecurityAnnotation("*.show")
    @PostMapping("treeSelect")
    open fun findTreeSelect(): Mono<Page<SysModuleModel>> {
        return sysModuleService.findTree()
    }
}