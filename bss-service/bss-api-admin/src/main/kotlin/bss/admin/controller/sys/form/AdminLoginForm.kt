package bss.admin.controller.sys.form

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

/**
 * 登录LoginForm
 */
data class AdminLoginForm(
    /**
     * 账号
     */
    @field:NotBlank
    @field:NotNull
    @field:Length(min = 3, max = 20)
    val account: String,
    /**
     * 密码
     */
    @field:NotBlank
    @field:NotNull
    @field:Length(min = 4, max = 25)
    val password: String,
)