package bss.core.auth

import bss.core.holder.LocalesHolder
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.Resource
import mu.KotlinLogging
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import bss.core.type.Locales
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*


private val logger = KotlinLogging.logger {}

private val DEFAULT_LOCALES = Locales.DEFAULT

open class LocalesFilter() : WebFilter {

    @Resource
    lateinit var objectMapper: ObjectMapper


    companion object {
        const val COOKIE_NAME: String = "locale"
        val LOCALES_CONTEXT_KEY = Locales::class.simpleName

        fun decode(cookie: String, objectMapper: ObjectMapper): Locales {
            val json = String(
                Base64.getDecoder().decode(
                    URLDecoder.decode(
                        cookie, Charsets.UTF_8
                    )
                ), Charsets.UTF_8
            )
            val jsonNode = objectMapper.readTree(json)
            return Locales.valueOf(jsonNode.get("locale").asText())
        }

        fun encode(locale: Locales, objectMapper: ObjectMapper): String {
            val json = objectMapper.writeValueAsString(mapOf("locale" to locale.name))
            val jsonBytes = json.toByteArray(Charsets.UTF_8)
            val encodedJson = Base64.getEncoder().encodeToString(jsonBytes)
            return URLEncoder.encode(encodedJson, Charsets.UTF_8)
        }

        fun getLocales(request: ServerHttpRequest, objectMapper: ObjectMapper): Locales {
            fun getDefault(): Locales {
//                val locales = request.headers.acceptLanguageAsLocales
//                return if (locales.isEmpty()) {
                return DEFAULT_LOCALES
//                } else {
//                    return Locales.values().find { it.name == locales[0].language } ?: Locales.en_US
//                }
            }
            return try {
                val value = request.cookies.getFirst(COOKIE_NAME)?.value ?: return getDefault()
                return decode(value, objectMapper)
            } catch (e: Throwable) {
                logger.info(e) { "getLocales.error:${request.path}" }
                DEFAULT_LOCALES
            }
        }

        fun getLocales(request: ServerRequest, objectMapper: ObjectMapper): Locales {
            val headers = request.headers()
            fun getDefault(): Locales {
                val locales = headers.acceptLanguage()
                return if (locales.isEmpty()) {
                    DEFAULT_LOCALES
                } else {
                    val locale = Locale.forLanguageTag(locales[0].getRange())
                    return Locales.values().find { it.name == locale.language } ?: DEFAULT_LOCALES
                }
            }
            return try {
                val value = request.cookies().getFirst(Companion.COOKIE_NAME)?.value ?: return getDefault()
                return decode(value, objectMapper)
            } catch (e: Throwable) {
                logger.info(e) { "getLocales.error:${request.path()}" }
                DEFAULT_LOCALES
            }
        }
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val locales = getLocales(exchange)
//        val future = handlerMapping.getHandler(exchange).toFuture()
        return doFilter(exchange, locales, chain)
    }

    private fun getLocales(exchange: ServerWebExchange): Locales {
        return getLocales(exchange.request, objectMapper)
    }


    private fun doFilter(
        exchange: ServerWebExchange,
        locales: Locales,
        chain: WebFilterChain,
    ): Mono<Void> {
        if (!exchange.attributes.containsKey(LOCALES_CONTEXT_KEY)) {
            exchange.attributes[LOCALES_CONTEXT_KEY] = locales
        }

        return chain
            .filter(exchange)
            .contextWrite {
                LocalesHolder.write(it, locales)
            }
    }
}