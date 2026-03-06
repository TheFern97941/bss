package bss.admin

import bss.core.annotation.NotSecurity
import bss.core.annotation.SecurityAnnotation
import bss.core.auth.AccountContext
import bss.core.auth.AccountManager
import bss.core.auth.AccountWebFilter
import bss.core.exception.MessageException
import bss.core.exception.MessageException.Companion.COMMON_ROLE_NOT_FOUND
import bss.core.holder.AccountContextHolder
import bss.service.services.sys.SecurityService
import jakarta.annotation.Resource
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


class AdminSecurityFilter(accountManager: AccountManager, handlerMapping: HandlerMapping) :
    AccountWebFilter(accountManager, handlerMapping) {

    @Resource
    private lateinit var securityService: SecurityService

    override fun doLoginFilter(
        exchange: ServerWebExchange,
        accountContext: AccountContext,
        chain: WebFilterChain,
        handler: HandlerMethod
    ): Mono<Void> {
        val path = exchange.request.path.value()

        val role = accountContext.role ?: throw COMMON_ROLE_NOT_FOUND

        val notSecurityAnno =
            AnnotatedElementUtils.findMergedAnnotation(handler.method, NotSecurity::class.java)
        if (notSecurityAnno != null) {
            exchange.attributes[SECURITY_CONTEXT_KEY] = accountContext
            return chain.filter(exchange).contextWrite { AccountContextHolder.write(it, accountContext) }
        }

//        val codes = findCodes(handler)
//        if (!codes.isNullOrEmpty() && securityService.check(role, codes)) {
//            exchange.attributes[SECURITY_CONTEXT_KEY] = accountContext
//            return chain.filter(exchange).contextWrite { AccountContextHolder.write(it, accountContext) }
//        } else {
//            throw MessageException(
//                "securityError",
//                "need auth code [path:$path, code:${codes}]"
//            )
//        }

        exchange.attributes[SECURITY_CONTEXT_KEY] = accountContext
        return chain.filter(exchange).contextWrite { AccountContextHolder.write(it, accountContext) }
    }

    private fun findCodes(handler: HandlerMethod): String? {
        val classAnnotation =
            AnnotatedElementUtils.findMergedAnnotation(handler.beanType, SecurityAnnotation::class.java)
        val methodAnnotation =
            AnnotatedElementUtils.findMergedAnnotation(handler.method, SecurityAnnotation::class.java)
        if (classAnnotation == null) {
            return methodAnnotation?.value
        }
        val parentModuleName = classAnnotation.value
        if(methodAnnotation?.value.isNullOrEmpty()){
            return classAnnotation.value
        }

        return methodAnnotation?.value?.let {
            if (it.startsWith("*.")) {
                parentModuleName + it.substring(1)
            } else {
                it
            }
        }
    }
}