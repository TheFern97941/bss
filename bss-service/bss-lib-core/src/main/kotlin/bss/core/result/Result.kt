package bss.core.result

import bss.core.exception.MessageException

/*
export interface response {
  success: boolean; // if request is success
  data?: any; // response data
  errorCode?: string; // code for errorType
  errorMessage?: string; // message display to user
  showType?: number; // error display type： 0 silent; 1 message.warn; 2 message.error; 4 notification; 9 page
  traceId?: string; // Convenient for back-end Troubleshooting: unique request ID
  host?: string; // onvenient for backend Troubleshooting: host of current access server
}
 */

data class Result<T>(
    val success: Boolean = true,
    val data: T? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null,
    val showType: Int? = 0,
    val path: String? = null,
) {
    companion object {
        fun <T> success(): Result<T> = Result(true, null)
        fun <T> success(t: T): Result<T> = Result(true, t)
        fun <T> error(code: String?, msg: String?, path: String?): Result<T> = Result(false, null, code, msg, 2, path)
        fun <T> error(m: MessageException, path: String?): Result<T> = Result(false, null, m.code, m.message, 2, path)
        fun <T> error(m: Throwable, path: String?): Result<T> = Result(false, null, "error", m.message, 2, path)
    }
}