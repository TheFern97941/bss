package bss.core.annotation

/**
 * 返回自定的包装Response
 * 我现在妥协了，也开始返回Result这种结果
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ResponseResult()
