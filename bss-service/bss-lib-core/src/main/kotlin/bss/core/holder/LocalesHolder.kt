package bss.core.holder

import bss.core.type.Locales
import reactor.core.publisher.Mono
import reactor.util.context.Context
import reactor.util.context.ContextView

private val localeKey = Locales::class
private const val userLocaleKey = "userLocaleKey"

object LocalesHolder {

    val context: Mono<Locales>
        get() {
            return Mono.deferContextual(this::getLocalesContext)
        }


    private fun getLocalesContext(context: ContextView): Mono<Locales> {
        val userLocale = context.getOrEmpty<Locales>(userLocaleKey)
        if (userLocale.isPresent) {
            return Mono.justOrEmpty(userLocale)
        }
        val locales = context.getOrDefault<Locales>(localeKey, Locales.DEFAULT)
        return Mono.justOrEmpty(locales)
//        return AccountContextHolder.context
//            .map { it.locale }
//            .switchIfEmpty {
//                val locales = context.getOrEmpty<Locales>(localeKey)
//                Mono.justOrEmpty(locales)
//            }
    }

    /**
     */
    fun write(ctx: Context, locales: Locales): Context {
        return ctx.put(localeKey, locales)
    }

    /**
     */
    fun writeUserLocale(ctx: Context, locales: Locales): Context {
        return ctx.put(userLocaleKey, locales)
    }
}