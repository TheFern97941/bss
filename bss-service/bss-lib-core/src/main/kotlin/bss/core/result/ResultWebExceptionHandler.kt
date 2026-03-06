package bss.core.result

import bss.core.exception.MessageException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.validation.ValidationException
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.web.ErrorProperties
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.FieldError
import org.springframework.validation.beanvalidation.SpringValidatorAdapter
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.method.HandlerMethod
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import java.util.*
import java.util.stream.Collectors

private val logger = KotlinLogging.logger {}

open class ResultWebExceptionHandler(
    private val responseResultPredicate: ResponseResultPredicate,
    private val requestMappingHandlerMapping: RequestMappingHandlerMapping,
    errorAttributes: ErrorAttributes?,
    resources: WebProperties.Resources?,
    errorProperties: ErrorProperties?,
    applicationContext: ApplicationContext?,
    val objectMapper: ObjectMapper
) : DefaultErrorWebExceptionHandler(
    errorAttributes, resources, errorProperties, applicationContext
) {
    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse> {
        val predicate = RequestPredicates.all()
        return RouterFunctions.route(predicate, ::renderErrorResponse)
    }

    override fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse> {
        val error = getError(request)
        if (error is ResponseStatusException) {
            return doRender(request)
        }

        val future = requestMappingHandlerMapping.getHandler(request.exchange()).toFuture()
        val now = future.getNow(null) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        if (responseResultPredicate.isResponseResult(now as HandlerMethod)) {
            return doRender(request)
        }
        return super.renderErrorResponse(request)
    }

    private fun doRender(request: ServerRequest): Mono<ServerResponse> {
        val error = when (val throwable = getError(request)) {
            is NoSuchElementException -> toResult(request, "error.notFound", "未找到查询内容", request.path())
            is MessageException -> toResult(request, throwable.code, throwable.message, request.path())
            is WebExchangeBindException -> toResult(request, "valid.error", toMessage(throwable), request.path())
            else -> toResult(request, "error", throwable.message, request.path())
        }
        return error.flatMap { body ->
            if (request.headers().accept().contains(MediaType.TEXT_EVENT_STREAM)) {
                ServerResponse.status(HttpStatus.OK).contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(BodyInserters.fromValue(body))
            } else {
                ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(body))
            }
        }
    }

    open fun toResult(request: ServerRequest, code: String, msg: String?, path: String): Mono<Result<Any>> {
        //
        return Mono.just(Result.error(code, msg, path))
    }

    open fun toMessage(throwable: WebExchangeBindException): String? {
        val errors = throwable.bindingResult.allErrors.stream().filter(Objects::nonNull).map {
            if (it is FieldError) {
                Pair(it.field, it.defaultMessage)
            } else {
                val code = it.codes?.first()?.split(".")
                Pair(code?.last(), it.defaultMessage)
            }
        }.collect(
            Collectors.toMap(
                {it.first},
                {it.second},
            ) { o, t ->
                "$o,$t"
            }
        )
        return objectMapper.writeValueAsString(errors)
    }

    override fun logError(request: ServerRequest, response: ServerResponse, throwable: Throwable?) {
        when (throwable) {
            is MessageException -> super.logError(request, response, throwable)
            is ValidationException -> super.logError(request, response, throwable)
            is ResponseStatusException -> super.logError(request, response, throwable)
            else -> {
                logger.error(throwable) { "服务器错误:${throwable.toString()}" }
                return super.logError(request, response, throwable)
            }
        }
    }

}