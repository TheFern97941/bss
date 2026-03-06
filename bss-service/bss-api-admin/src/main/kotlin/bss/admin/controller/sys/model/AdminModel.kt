package bss.admin.controller.sys.model

import bss.core.type.Locales
import org.bson.types.ObjectId

data class AdminModel(
    val id: ObjectId,
    val locale: Locales,
    val name: String,
    val avatar: String?,
    val username: String,
    val title: String?,
    val bindAuthKey: Boolean = false,
    val role: String? = null,
    val modules: Set<String>,
)
