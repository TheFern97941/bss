package bss.core.service.account.form

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class ChangePwdForm(

    @NotBlank
    @Length(min = 6, max = 20)
    val password: String,

    @NotBlank
    @Length(min = 6, max = 20)
    val oldPassword: String,
)