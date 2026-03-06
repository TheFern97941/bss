package bss.admin.controller

import bss.admin.BssBaseTest
import bss.core.page.PageQuery
import bss.core.service.account.form.ChangePwdForm
import bss.core.service.account.form.LoginForm
import bss.service.entity.sys.LoginLog
import bss.service.entity.sys.SysModule
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

abstract class BaseControllerTest<T>: BssBaseTest() {

    abstract fun getPath(): String
    
    fun findById(id: String): WebTestClient.ResponseSpec {
        return webClient.get()
            .uri("${getPath()}/$id")
            .header(tokenHeaderName, token)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
    }

    
    fun page(pq: PageQuery): WebTestClient.ResponseSpec {
        return webClient.post()
            .uri(getPath())
            .header(tokenHeaderName, token)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(objectMapper.writeValueAsString(pq))
            .exchange()
            .expectStatus().isOk
    }

    
    fun delete(id: String): WebTestClient.ResponseSpec {
        return webClient.delete()
            .uri("${getPath()}/$id")
            .header(tokenHeaderName, token)
            .exchange()
            .expectStatus().isOk
    }

    
    fun update(body: T, id: String): WebTestClient.ResponseSpec {
        return webClient.post()
            .uri("${getPath()}/${id}")
            .contentType(MediaType.APPLICATION_JSON)
            .header(tokenHeaderName, token)
            .bodyValue(objectMapper.writeValueAsString(body))
            .exchange()
            .expectStatus().isOk
    }

    
    fun create(body: T): WebTestClient.ResponseSpec {
        return webClient.post()
            .uri("${getPath()}/create")
            .contentType(MediaType.APPLICATION_JSON)
            .header(tokenHeaderName, token)
            .bodyValue(objectMapper.writeValueAsString(body))
            .exchange()
            .expectStatus().isOk
    }
}