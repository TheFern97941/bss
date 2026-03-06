package bss.core.controller.form

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class BindGoogleAuthForm (
    @field:NotBlank
    var authKey: String,
    @field:NotNull
    var authCode: Int
)
