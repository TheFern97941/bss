package bss.admin.controller

import bss.core.annotation.NotAuth
import bss.core.annotation.RestService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger {}

@RestService(
    requestPath = "test",
    securityValue = "test",
)
class HelloController {

    @RequestMapping("hello")
    @NotAuth
    fun sayHello(): Mono<String> {
        logger.info { "hello info --" }
        logger.debug { "hello debug --" }
        return Mono.just("Hello kotlin");
    }
}