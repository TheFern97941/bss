package bss.core.annotation

import org.springframework.core.annotation.AliasFor
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@RestController
@ResponseBody
@ResponseResult
@Auth
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@RequestMapping
@SecurityAnnotation
annotation class RestService(
    @get:AliasFor(annotation = RequestMapping::class, attribute = "path")
    val requestPath: String,

    @get:AliasFor(annotation = SecurityAnnotation::class, attribute = "value")
    val securityValue: String
)
