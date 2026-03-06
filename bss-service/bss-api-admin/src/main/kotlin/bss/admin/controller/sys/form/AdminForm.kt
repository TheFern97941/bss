package bss.admin.controller.sys.form

import bss.service.enums.StatusEnums
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

data class AdminForm(
    @field:NotBlank
    @field:Length(min = 3, max = 20)
    val username: String,
    val password: String? = null,
    @NotNull
    var status: StatusEnums = StatusEnums.NORMAL,

    var role: String? = "guest",

    /**
     * 是否為機器人管理員
     */
    val isRobotAdmin: Boolean = false,

    var name: String? = null,
    var title: String? = null,
    var email: String? = null,
    var avatar: String? = null,
)