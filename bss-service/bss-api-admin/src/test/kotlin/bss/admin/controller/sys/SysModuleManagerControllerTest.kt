package bss.admin.controller.sys

import bss.admin.controller.BaseControllerTest
import bss.core.page.PageQuery
import bss.service.entity.sys.LoginLog
import bss.service.entity.sys.SysModule
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.data.domain.Page
import org.springframework.test.web.reactive.server.expectBody

@AutoConfigureWebTestClient
class SysModuleManagerControllerTest: BaseControllerTest<SysModule>() {

    override fun getPath(): String = "/sysModule"

    @Test
    fun findById() {
        super.findById("test22")
            .expectBody<Result<SysModule>>()
            .consumeWith {
                println("Response body: $it")
            }
    }

    @Test
    fun page() {
        val pq = PageQuery()

        super.page(pq)
            .expectBody<Result<Page<LoginLog>>>()
            .consumeWith {
                println("Response body: $it")
            }
    }

    @Test
    fun delete() {
        val id = "test22"
        super.delete(id)
            .expectBody<String>()
            .consumeWith {
                println("Response body: $it")
            }
    }

    @Test
    fun update() {
        val sysModule = SysModule(id="test22", name = "test233");
        super.update(sysModule, sysModule.id)
            .expectBody<String>()
            .consumeWith {
                println("Response body: $it")
            }
    }

    @Test
    fun create() {
        val sysModule = SysModule(id="test22", name = "test22");
        super.create(sysModule)
            .expectBody<String>()
            .consumeWith {
                println("Response body: $it")
            }
    }
}