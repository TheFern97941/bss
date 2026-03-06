package bss.core.holder

import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.util.context.Context
import reactor.util.context.ContextView
import java.util.*

private val serverWebExchangeKey = ExchangeHolder::class

object ExchangeHolder {

    /**
     *
     */
    val context: Mono<ServerWebExchange>
        get() {
            return Mono.deferContextual(this::getSecurityContext)
        }

    val contextOptional: Mono<Optional<ServerWebExchange>>
        get() {
            return Mono.deferContextual(this::getSecurityContextOptional)
        }

    private fun getSecurityContextOptional(context: ContextView): Mono<Optional<ServerWebExchange>> {
        val ServerWebExchange = context.getOrEmpty<ServerWebExchange>(serverWebExchangeKey)
        return Mono.just(ServerWebExchange)
    }

    private fun getSecurityContext(context: ContextView): Mono<ServerWebExchange> {
        val ServerWebExchange = context.getOrEmpty<ServerWebExchange>(serverWebExchangeKey)
        return Mono.justOrEmpty(ServerWebExchange)
    }

    /**
     */
    fun write(ctx: Context, ServerWebExchange: ServerWebExchange): Context {
        return ctx.put(serverWebExchangeKey, ServerWebExchange)
    }
}