package bss.core.service.account.form

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

/**
 * 登录LoginForm
 */
data class LoginForm(
    /**
     * 账号
     */
    @field:NotBlank
    @field:NotNull
    @field:Length(min = 3, max = 256)
    val account: String,
    /**
     * 密码
     */
    @field:NotBlank
    @field:NotNull
    @field:Length(min = 4, max = 32)
    val password: String,

    var authCode: String? = null,
)