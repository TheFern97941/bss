package bss.core.properties

class Auth(
    var authLoginErrorMax: Int = 6,
    var authLoginTokenMax: Int = 8,
    var timeOutHours: Long = 48,
    // 29天
    var clientTimeOutHours: Long = 22 * 24,
    var timeOutDelayMinutes: Long = 48 * 60,
)