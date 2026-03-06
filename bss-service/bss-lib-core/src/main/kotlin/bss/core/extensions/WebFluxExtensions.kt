package bss.core.extensions

import org.springframework.web.filter.reactive.ServerWebExchangeContextFilter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import reactor.util.context.ContextView


fun getServerWebExchange(ctx: ContextView): Mono<ServerWebExchange> {
    return ctx.get<ServerWebExchange>(ServerWebExchangeContextFilter.EXCHANGE_CONTEXT_ATTRIBUTE).toMono()
}

