package bss.service.services.sys.form

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class SysRoleForm(
    @field:NotEmpty
    @field:NotNull
    val id: String,
    @field:NotEmpty
    @field:NotNull
    val name: String,
    val moduleId: List<String> = mutableListOf()
)