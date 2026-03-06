package bss.core.exception

import reactor.core.publisher.Mono
import java.lang.RuntimeException

open class MessageException(val code: String, override val message: String, cause: Throwable? = null) :
    RuntimeException(message, cause) {
    override fun toString(): String {
        return "MessageThrowable(code='$code', message='$message')"
    }

    // 一些通用的异常, 省的来回复制, 写错,
    // 其他自定义的还是要自己new
    companion object {
        val COMMON_PWD_NOT_MATCH = MessageException("password.notMatch", "password do not match")
        val COMMON_LOGIN_NOT_FOUND = MessageException("loginNotFound", "username or password not found")
        val COMMON_LOGIN_LOCKED = MessageException("login.lock", "account is locked")
        val COMMON_LOGIN_TIMEOUT = MessageException("token.timeOut", "timeout")
        val COMMON_ROLE_NOT_FOUND = MessageException("role.notFind", "role not found")
        val COMMON_AUTH_CODE_NOT_MATCH = MessageException("authCodeError", "auth code error")

    }
}

fun <T> messageException(code: String, message: String): Mono<T> {
    return Mono.defer { Mono.error(MessageException(code, message)) }
}

inline fun require(value: Boolean, code: String, lazyMessage: () -> Any) {
    if (!value) {
        val message = lazyMessage()
        throw MessageException(code, message.toString())
    }
}