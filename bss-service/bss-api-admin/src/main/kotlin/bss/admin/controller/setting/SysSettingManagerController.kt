package bss.admin.controller.setting

import bss.core.annotation.RestService
import bss.core.utils.DataClassUtils
import bss.service.entity.setting.AllSetting
import bss.service.entity.setting.AuthSetting
import bss.service.services.setting.SettingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Mono

@RestService(requestPath = "sysSetting", securityValue = "sysSetting")
class SysSettingManagerController(
    private val settingService: SettingService,
) {

    @GetMapping("{id}")
    fun getSettingById(
        @PathVariable
        id: String
    ): AllSetting? {
        return settingService.findById(id)
    }

    @PostMapping("/authSetting")
    fun authSetting(
        @RequestBody setting: AuthSetting
    ): Mono<AllSetting> {
        return settingService.save(DataClassUtils.copy(setting, settingService.authSetting()))
    }

}