package bss.service.services.sys.form

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

class SysModuleForm (
    @field:NotEmpty
    @field:NotNull
    var id: String,
    var path: String? = null,
    @field:NotEmpty
    @field:NotNull
    var name: String,
    var parent: String? = null
)