package bss.service.entity.setting

/**
 *
 */
data class AuthSetting(
    var authLoginErrorMax: Int = 6,
    var authLoginTokenMax: Int = 1,
    var timeOutHours: Long = 48,
    var timeOutDelayMinutes: Long = 48 * 60,
    var sessionTimeOutHours: Long = 365 * 24,

    /**
     * 密码输入错误2次后需要验证码
     */
    var loginNeedAuthCodeCount: Int = 2,

    var sessionMax: Int = 1,

    ) : Setting
