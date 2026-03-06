package bss.admin.controller.sys

import bss.admin.controller.BaseControllerTest
import bss.admin.controller.sys.model.AdminModel
import bss.core.page.PageQuery
import bss.service.entity.sys.Admin
import bss.service.entity.sys.LoginLog
import bss.service.entity.sys.SysModule
import bss.service.entity.sys.SysRole
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.data.domain.Page
import org.springframework.test.web.reactive.server.expectBody
import java.util.*

@AutoConfigureWebTestClient
class SysAdminManagerControllerTest: BaseControllerTest<Admin>() {

    override fun getPath(): String = "/sysAdmin"

    @Test
    fun findById() {
        super.findById(uId.toHexString())
            .expectBody<Result<AdminModel>>()
            .consumeWith {
                println("[findById] body: $it")
            }
    }

    @Test
    fun page() {
        val pq = PageQuery()

        super.page(pq)
            .expectBody<Result<Page<AdminModel>>>()
            .consumeWith {
                println("[page] body: $it")
            }
    }

    @Test
    fun delete() {
        super.delete(uId.toHexString())
            .expectBody<String>()
            .consumeWith {
                println("[delete] body: $it")
            }
    }

    val uId = ObjectId("680f32d6c12ca7349647162e")

    @Test
    fun update() {
        val sysModule = Admin(id=uId, name = "test23333", username = "admin3", password = "admin22");
        super.update(sysModule, sysModule.id.toHexString())
            .expectBody<String>()
            .consumeWith {
                println("[update] body: $it")
            }
    }

    @Test
    fun create() {
        val sysModule = Admin(id=uId, name = "test233", username = "admin2", password = "admin");
        super.create(sysModule)
            .expectBody<String>()
            .consumeWith {
                println("[create] body: $it")
            }
    }
}