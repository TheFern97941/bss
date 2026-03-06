package bss.core.result

import bss.core.annotation.ResponseResult
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod

@Component
class ResponseResultPredicate {
    val cache: HashMap<HandlerMethod, Boolean> = hashMapOf()
    fun isResponseResult(handlerMethod: HandlerMethod): Boolean {
        return cache[handlerMethod] ?: checkIsResponseResult(handlerMethod);
    }

    private fun checkIsResponseResult(handlerMethod: HandlerMethod): Boolean {
        val containingClass = handlerMethod.returnType.containingClass
        val isResponseResult = AnnotatedElementUtils.hasAnnotation(containingClass, ResponseResult::class.java) ||
                handlerMethod.hasMethodAnnotation(ResponseResult::class.java)
        cache[handlerMethod] = isResponseResult
        return isResponseResult;
    }
}