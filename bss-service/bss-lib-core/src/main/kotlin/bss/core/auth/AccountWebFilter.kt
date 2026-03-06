package bss.core.auth

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.http.MediaType
import org.springframework.web.method.HandlerMethod
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import bss.core.annotation.Auth
import bss.core.annotation.NotAuth
import bss.core.exception.MessageException
import bss.core.exception.MessageException.Companion.COMMON_LOGIN_TIMEOUT
import bss.core.holder.AccountContextHolder


interface AccountManager {
    fun auth(token: String): Mono<AccountContext>
}

private val logger = KotlinLogging.logger {}

open class AccountWebFilter(
    open var accountManager: AccountManager,
    open var handlerMapping: HandlerMapping,
) : WebFilter {

    @Autowired
    lateinit var objectMapper: ObjectMapper
//    @Autowired
//    lateinit var accountCookiesService: AccountCookiesService


    companion object {
        const val TOKEN_AUTH_HEADER_NAME: String = "authorization"
        val SECURITY_CONTEXT_KEY = AccountContext::class.simpleName
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val token = getToken(exchange)
        val future = handlerMapping.getHandler(exchange).toFuture()
        val handlerObject = future.getNow(null)

        if (handlerObject != null && handlerObject is HandlerMethod) {
            if (handlerObject.hasMethodAnnotation(NotAuth::class.java)) {
                return if (token != null) {
                    accountManager.auth(token)
                        .flatMap { doLoginFilter(exchange, it, chain,handlerObject) }
                        .onErrorResume(
                            { e -> e is MessageException && COMMON_LOGIN_TIMEOUT.code == e.code },
                            { _ -> chain.filter(exchange) }
                        )
                } else {
                    chain.filter(exchange)
                }
            } else {
                val containingClass = handlerObject.returnType.containingClass
                val isAuth = AnnotatedElementUtils.hasAnnotation(
                    containingClass, Auth::class.java
                ) || handlerObject.hasMethodAnnotation(Auth::class.java)

                return if (isAuth) {
                    if (token != null) {
                        accountManager.auth(token).flatMap { doLoginFilter(exchange, it, chain, handlerObject) }
                    } else {
                        COMMON_LOGIN_TIMEOUT.toMono()
                    }
                } else {
                    chain.filter(exchange)
                }
            }
        } else {
//            logger.info { "exchange.filter.ignore:${exchange.request.path}" }
            return chain.filter(exchange)
        }
    }

    /**
     * 登录之后的过滤流程
     */
    open protected fun doLoginFilter(
        exchange: ServerWebExchange,
        accountContext: AccountContext,
        chain: WebFilterChain,
        handler: HandlerMethod,
    ): Mono<Void> {
        exchange.attributes[SECURITY_CONTEXT_KEY] = accountContext
        return chain.filter(exchange).contextWrite { AccountContextHolder.write(it, accountContext) }
    }

    private fun getToken(exchange: ServerWebExchange): String? {
        try {
            var token = exchange.request.headers.getFirst(TOKEN_AUTH_HEADER_NAME)

            if (token == null && exchange.request.headers.accept.contains(MediaType.TEXT_EVENT_STREAM)) {
                token = exchange.request.queryParams.getFirst("\$token")
            }
            //Cookie: token=eyJ0b2tlbiI6IlZXME83M3paYW1iUmliaXI4ZXZkNlVJRm1Pcmt5QmZxIn0%3D
//            if (token == null) {
//                token = accountCookiesService.decode(exchange.request.cookies.getFirst(AccountCookiesService.COOKIE_KEY_NAME)?.value)
//            }
            return token
        } catch (e: Throwable) {
            logger.info(e) { "getToken.error:${exchange.request.path}" }
            return null
        }
    }
}