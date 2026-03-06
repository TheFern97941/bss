package bss.core.result

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.method.HandlerMethod
import org.springframework.web.reactive.HandlerResult
import org.springframework.web.reactive.accept.RequestedContentTypeResolver
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

class ResultResponseBodyResultHandler(writers: List<HttpMessageWriter<*>>, resolver: RequestedContentTypeResolver) :
    ResponseBodyResultHandler(writers, resolver) {

    @Autowired
    private lateinit var responseResultPredicate: ResponseResultPredicate

    override fun supports(result: HandlerResult): Boolean {
        val handler = result.handler
        if (responseResultPredicate.isResponseResult(handler as HandlerMethod)) {
            return true
        }
        return false
    }

    override fun handleResult(exchange: ServerWebExchange, result: HandlerResult): Mono<Void> {
        val accept = exchange.request.headers.accept
        if (accept.contains(MediaType.TEXT_EVENT_STREAM) || accept.contains(MediaType.APPLICATION_OCTET_STREAM)) {
            return super.handleResult(exchange, result)
        }
        val methodAnnotation = (result.handler as HandlerMethod).getMethodAnnotation(RequestMapping::class.java)
        if (methodAnnotation != null) {
            // 判断方法有没有生产头
            if (methodAnnotation.produces.contains(MediaType.APPLICATION_OCTET_STREAM_VALUE)) {
                return super.handleResult(exchange, result)
            }
        }

        val body = when (val value = result.returnValue) {
            is Mono<*> -> value
            is Flux<*> -> {
                value.collectList()
            }

            else -> {
                return writeBody(success(value), result.returnTypeSource, exchange)
            }
        }
            .map { r -> success(r) }
            .switchIfEmpty {
                Result.success<Any?>().toMono()
            }

        return writeBody(body, returnType, exchange)
    }

    private fun success(r: Any?): Any {
        if (r is ByteArray) {
            return r
        } else if (r is ServerResponse) {
            return r
        }
        return Result.success(r)
    }

    companion object {
        @JvmStatic
        private fun methodForReturnType(): Mono<Result<Any>>? = null

        private val returnType: MethodParameter = MethodParameter(
            ResultResponseBodyResultHandler::class.java.getDeclaredMethod("methodForReturnType"), -1
        )
    }
}