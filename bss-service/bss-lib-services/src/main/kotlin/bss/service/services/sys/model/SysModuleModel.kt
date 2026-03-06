package bss.service.services.sys.model

import java.time.LocalDateTime

data class SysModuleModel(
    val id: String,
    val parent: String? = null,
    val path: String? = null,
    val name: String? = null,
    var children: MutableList<SysModuleModel>? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
)
