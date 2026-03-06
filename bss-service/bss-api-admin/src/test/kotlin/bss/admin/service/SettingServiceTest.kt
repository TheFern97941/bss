package bss.admin.service

import bss.service.entity.sys.SysModule
import bss.service.entity.sys.SysRole
import bss.service.repository.sys.SysModuleRepository
import bss.service.repository.sys.SysRoleRepository
import bss.service.services.setting.SettingService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.switchIfEmpty

// junit 4 需要
// @RunWith(SpringRunner::class)
@SpringBootTest(
    // 如果服务在启动状态, 就随机一个端口, 启动测试
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@AutoConfigureWebTestClient
class SettingServiceTest {

    @Autowired
    lateinit var settingService: SettingService

    @Test
    fun taskTest() {
        println(settingService.authSetting())
    }

}

