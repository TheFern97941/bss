package bss.service.services.sys.model

import java.time.LocalDateTime

data class SysModuleLeaf(
    val id: String,
    val parent: String? = null,
    val path: String? = null,
    val name: String? = null,
    val children: MutableList<SysModuleLeaf> = mutableListOf(),
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)
