package bss.core

import bss.core.auth.AccountHandlerMethodArgumentResolver
import bss.core.openapi.PageSupportConverter
import bss.core.properties.CoreProperties
import bss.core.result.ResponseResultPredicate
import bss.core.result.ResultResponseBodyResultHandler
import bss.core.result.ResultWebExceptionHandler
import bss.core.service.cache.EntityChangeService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import bss.core.ExchangeHolderFilter
import bss.core.openapi.ObjectIdModelConverter
import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils
import org.bson.types.ObjectId
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.providers.ObjectMapperProvider
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.*
import org.springframework.core.Ordered
import org.springframework.core.ReactiveAdapterRegistry
import org.springframework.core.annotation.Order
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.accept.RequestedContentTypeResolver
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping
import org.springframework.web.reactive.result.view.ViewResolver
import java.util.stream.Collectors

private val logger = KotlinLogging.logger {}

@Configuration
@EnableConfigurationProperties(CoreProperties::class)
class CoreAuthConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun exchangeHolderFilter() = ExchangeHolderFilter()


    @Bean
    fun objectIdEntityChangeService(reactiveStringRedisTemplate: ReactiveStringRedisTemplate): EntityChangeService<ObjectId> {
        return EntityChangeService(reactiveStringRedisTemplate = reactiveStringRedisTemplate,
            toString = { it.toHexString() },
            toKey = { ObjectId(it) })
    }

    @Bean
    fun stringEntityChangeService(reactiveStringRedisTemplate: ReactiveStringRedisTemplate): EntityChangeService<String> {
        return EntityChangeService(reactiveStringRedisTemplate = reactiveStringRedisTemplate,
            toString = { it },
            toKey = { it })
    }

    @Bean
    fun intEntityChangeService(reactiveStringRedisTemplate: ReactiveStringRedisTemplate): EntityChangeService<Int> {
        return EntityChangeService(reactiveStringRedisTemplate = reactiveStringRedisTemplate,
            toString = { it.toString() },
            toKey = { NumberUtils.toInt(it) })
    }

    @Bean
    fun resources(): WebProperties.Resources? {
        return WebProperties.Resources()
    }

    @Bean
    fun accountHandlerMethodArgumentResolver(adapterRegistry: ReactiveAdapterRegistry) =
        AccountHandlerMethodArgumentResolver(adapterRegistry)

    @Bean
    fun kotlinModule(): KotlinModule? {
        return KotlinModule.Builder().withReflectionCacheSize(512).configure(KotlinFeature.NullToEmptyCollection, true)
            .configure(KotlinFeature.NullToEmptyMap, true).configure(KotlinFeature.NullIsSameAsDefault, enabled = true)
            .build()
    }

    @Bean
    fun resultResponseBodyResultHandler(
        resolver: ServerCodecConfigurer,
        requestedContentTypeResolver: RequestedContentTypeResolver,
    ): ResultResponseBodyResultHandler {
        return ResultResponseBodyResultHandler(resolver.writers, requestedContentTypeResolver)
    }

    @Bean
    @Order(-1)
    @ConditionalOnMissingBean(name = ["errorWebExceptionHandler"])
    fun errorWebExceptionHandler(
        responseResultPredicate: ResponseResultPredicate,
        requestMappingHandlerMapping: RequestMappingHandlerMapping,
        serverProperties: ServerProperties?,
        errorAttributes: ErrorAttributes?,
        webProperties: WebProperties, viewResolvers: ObjectProvider<ViewResolver?>,
        serverCodecConfigurer: ServerCodecConfigurer, applicationContext: ApplicationContext?,
        objectMapper: ObjectMapper
    ): ErrorWebExceptionHandler? {
        val exceptionHandler = ResultWebExceptionHandler(
            responseResultPredicate,
            requestMappingHandlerMapping,
            errorAttributes,
            webProperties.resources,
            serverProperties?.error,
            applicationContext,
            objectMapper
        )
        exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()))
        exceptionHandler.setMessageWriters(serverCodecConfigurer.writers)
        exceptionHandler.setMessageReaders(serverCodecConfigurer.readers)
        return exceptionHandler
    }

    // openapi converter
    @Bean
    fun pageSupportConverter(objectMapperProvider: ObjectMapperProvider): PageSupportConverter {
        return PageSupportConverter(objectMapperProvider)
    }

    @Bean
    fun objectIdModelConverter(objectMapperProvider: ObjectMapperProvider): ObjectIdModelConverter {
        return ObjectIdModelConverter(objectMapperProvider)
    }

    @Bean
    fun openApiCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            openApi.paths.forEach { (path, pathItem) ->
                val suffix = StringUtils.split(path, "/")
                    .filter { StringUtils.isNotBlank(it) }
                    .filter { !StringUtils.startsWith(it, "{") }
                    .joinToString(separator = "") { it.replaceFirstChar(Char::titlecase) }

                pathItem.readOperationsMap().forEach { (httpMethod, operation) ->
                    operation.operationId = "${httpMethod.name.lowercase()}$suffix"
                }
            }
        }
    }
}