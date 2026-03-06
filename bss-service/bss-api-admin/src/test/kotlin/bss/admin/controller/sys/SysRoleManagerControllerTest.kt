package bss.admin.controller.sys

import bss.admin.controller.BaseControllerTest
import bss.core.page.PageQuery
import bss.service.entity.sys.LoginLog
import bss.service.entity.sys.SysModule
import bss.service.entity.sys.SysRole
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.data.domain.Page
import org.springframework.test.web.reactive.server.expectBody
import java.util.*

@AutoConfigureWebTestClient
class SysRoleManagerControllerTest: BaseControllerTest<SysRole>() {

    override fun getPath(): String = "/sysRole"

    @Test
    fun findById() {
        super.findById("test22")
            .expectBody<Result<SysModule>>()
            .consumeWith {
                println("[findById] body: $it")
            }
    }

    @Test
    fun page() {
        val pq = PageQuery()

        super.page(pq)
            .expectBody<Result<Page<LoginLog>>>()
            .consumeWith {
                println("[page] body: $it")
            }
    }

    @Test
    fun delete() {
        val id = "test22"
        super.delete(id)
            .expectBody<String>()
            .consumeWith {
                println("[delete] body: $it")
            }
    }

    @Test
    fun update() {
        val sysModule = SysRole(id="test22", name = "test233", moduleId = Collections.emptySet<String>().toHashSet());
        super.update(sysModule, sysModule.id)
            .expectBody<String>()
            .consumeWith {
                println("[update] body: $it")
            }
    }

    @Test
    fun create() {
        val sysModule = SysRole(id="test22", name = "test22", moduleId = Collections.emptySet<String>().toHashSet());
        super.create(sysModule)
            .expectBody<String>()
            .consumeWith {
                println("[create] body: $it")
            }
    }
}