package bss.admin.repository

import bss.service.entity.sys.SysModule
import bss.service.entity.sys.SysRole
import bss.service.repository.sys.SysModuleRepository
import bss.service.repository.sys.SysRoleRepository
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
class SysRepositoryTest {

    @Autowired
    lateinit var sysRoleRepository: SysRoleRepository

    @Autowired
    lateinit var sysModuleRepository: SysModuleRepository

    @Test
    fun helloTest() {
        val sys = SysModule("sys", null, null, "sys")
        val sysChildList = listOf(
            SysModule("sysAdmin", "sys", "sysAdmin", "sysAdmin"),
            SysModule("sysRole", "sys", "sysRole", "sysRole"),
            SysModule("sysModule", "sys", "sysModule", "sysModule"),
            SysModule("account", "sys", "account", "account"),
            SysModule("testDb", "test", "testDb", "testDb"),
        )

        val test = SysModule("test", null, null, "开发测试")

        sysModuleRepository.findById(test.id)
            .switchIfEmpty {
                sysModuleRepository.save(test)
            }
            .doOnNext {
                println("testDb: $it")
            }
            .block()

        sysModuleRepository.findById(sys.id)
            .switchIfEmpty {
                sysModuleRepository.save(sys)
            }
            .doOnNext {
                println("sys: $it")
            }
            .block()

        Flux.fromIterable(sysChildList)
            .map {
                val n = mutableListOf(it)
                n.add(SysModule("${it.id}.create", it.id, null, "${it.id}.create"))
                n.add(SysModule("${it.id}.show", it.id, null, "${it.id}.show"))
                n.add(SysModule("${it.id}.delete", it.id, null, "${it.id}.delete"))
                n.add(SysModule("${it.id}.update", it.id, null, "${it.id}.update"))
                n
            }
            .flatMap { Flux.fromIterable(it) }
            .flatMap {
                sysModuleRepository.findById(it.id)
                    .switchIfEmpty {
                        sysModuleRepository.save(it)
                    }
                    .doOnNext {
                        println("${it.id}: $it")
                    }
            }
            .collectList()
            .block()

        val allModuleIds = sysModuleRepository.findAll()
            .map { it.id }
            .collectList()
            .block()
            ?.toHashSet()

        val adminRole = SysRole("admin", "admin", allModuleIds!!)

        sysRoleRepository.findById(adminRole.id)
            .flatMap {
                it.moduleId = allModuleIds
                sysRoleRepository.save(it)
            }
            .switchIfEmpty {
                sysRoleRepository.save(adminRole)
            }
            .doOnNext {
                println("${it.id}: $it")
            }
            .block()
    }

    @Test
    fun exampleTest(@Autowired webClient: WebTestClient) {
        webClient
            .get().uri("/hello")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>().isEqualTo("Hello kotlin")
    }
}

