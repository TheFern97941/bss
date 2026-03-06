package bss.admin.controller.sys

import bss.admin.BssBaseTest
import bss.core.service.account.form.ChangePwdForm
import bss.core.service.account.form.LoginForm
import bss.service.entity.sys.LoginLog
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.expectBody

@AutoConfigureWebTestClient
class AdminAccountControllerTest: BssBaseTest() {



    @Test
    fun login() {
        webClient.post()
            .uri("/account/login")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(objectMapper.writeValueAsString(LoginForm(account = "admin", password = "admin2")))
            .exchange()
            .expectStatus().isOk
            .expectBody<Result<LoginLog>>()
            .consumeWith {
                println("Response body: $it")
            }
    }

    @Test
    fun logout() {
        webClient.post()
            .uri("/account/logout")
            .contentType(MediaType.APPLICATION_JSON)
            .header(tokenHeaderName, token)
            .bodyValue(objectMapper.writeValueAsString(LoginForm(account = "admin", password = "admin")))
            .exchange()
            .expectStatus().isOk
            .expectBody<String>()
            .consumeWith {
                println("Response body: $it")
            }
    }

    @Test
    fun getCurrent() {
        webClient.get()
            .uri("/account")
            .header(tokenHeaderName, token)
            .exchange()
            .expectStatus().isOk
            .expectBody<String>()
            .consumeWith {
                println("Response body: $it")
            }
    }

    @Test
    fun startGoogleAuth() {
        webClient.post()
            .uri("/account/startGoogleAuth")
            .contentType(MediaType.APPLICATION_JSON)
            .header(tokenHeaderName, token)
            .exchange()
            .expectStatus().isOk
            .expectBody<String>()
            .consumeWith {
                println("Response body: $it")
            }
    }

    @Test
    fun changePwd() {
        val pwdForm = ChangePwdForm(password = "admin2", oldPassword = "admin1");
        webClient.post()
            .uri("/account/pwd")
            .contentType(MediaType.APPLICATION_JSON)
            .header(tokenHeaderName, token)
            .bodyValue(objectMapper.writeValueAsString(pwdForm))
            .exchange()
            .expectStatus().isOk
            .expectBody<String>()
            .consumeWith {
                println("Response body: $it")
            }
    }
}