package bss.core.holder

import bss.core.auth.AccountContext
import reactor.core.publisher.Mono
import reactor.util.context.Context
import reactor.util.context.ContextView
import java.util.*

private val accountContextKey = AccountContext::class

object AccountContextHolder {

    /**
     *
     */
    val context: Mono<AccountContext>
        get() {
            return Mono.deferContextual(this::getSecurityContext)
        }

    val contextOptional: Mono<Optional<AccountContext>>
        get() {
            return Mono.deferContextual(this::getSecurityContextOptional)
        }

    private fun getSecurityContextOptional(context: ContextView): Mono<Optional<AccountContext>> {
        val accountContext = context
            .getOrEmpty<AccountContext>(accountContextKey)
        return Mono.just(accountContext)
    }
    
    private fun getSecurityContext(context: ContextView): Mono<AccountContext> {
        val accountContext = context
            .getOrEmpty<AccountContext>(accountContextKey)
        return Mono.justOrEmpty(accountContext)
    }

    /**
     */
    fun write(ctx: Context, accountContext: AccountContext): Context {
        return ctx.put(accountContextKey, accountContext)
    }
}