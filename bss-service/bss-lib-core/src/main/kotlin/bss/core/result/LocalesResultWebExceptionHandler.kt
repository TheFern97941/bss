package bss.core.result

import bss.core.result.ResponseResultPredicate
import bss.core.result.ResultWebExceptionHandler
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.web.ErrorProperties
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping

open class LocalesResultWebExceptionHandler(
    private val responseResultPredicate: ResponseResultPredicate,
    private val requestMappingHandlerMapping: RequestMappingHandlerMapping,
    errorAttributes: ErrorAttributes?,
    resources: WebProperties.Resources?,
    errorProperties: ErrorProperties?,
    applicationContext: ApplicationContext?,
    objectMapper: ObjectMapper,
) : ResultWebExceptionHandler(
    responseResultPredicate,
    requestMappingHandlerMapping,
    errorAttributes,
    resources,
    errorProperties,
    applicationContext,
    objectMapper
) {

}