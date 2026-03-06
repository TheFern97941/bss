package bss.core.service.account.form

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class RegForm(
    @NotBlank
    @field:Length(min = 3, max = 20)
    val account: String,
    @NotBlank
    @field:Length(min = 4, max = 25)
    val password: String,
)