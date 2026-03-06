package bss.core.auth

import bss.core.holder.AccountContextHolder
import org.springframework.core.MethodParameter
import org.springframework.core.ReactiveAdapterRegistry
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolverSupport
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class AccountHandlerMethodArgumentResolver(adapterRegistry: ReactiveAdapterRegistry) :
    HandlerMethodArgumentResolverSupport(adapterRegistry) {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return checkParameterTypeNoReactiveWrapper(parameter) {
            Account::class.java.isAssignableFrom(it) || AccountContext::class.java.isAssignableFrom(it)
        }
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange,
    ): Mono<Any?> {
        val paramType: Class<*> = parameter.parameterType

        return when {
            AccountContext::class.java.isAssignableFrom(paramType) -> {
                AccountContextHolder.context.cast(Any::class.java)
            }

            Account::class.java.isAssignableFrom(paramType) -> {
                AccountContextHolder.context.map { r -> r.account() }
            }

            else -> {
                throw IllegalArgumentException(
                    "Unknown parameter type: " + paramType + " in method: " + parameter.method
                )
            }
        }
    }

}