package bss.service.entity.sys

import bss.core.auth.ExpandAccount
import bss.core.type.Locales
import bss.service.enums.StatusEnums
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Admin(
    @Id
    override val id: ObjectId = ObjectId.get(),

    override val locale: Locales = Locales.DEFAULT,

    @Indexed(unique = true)
    override val username: String,

    override var password: String,

    val name: String = "",

    @Indexed
    var status: StatusEnums = StatusEnums.NORMAL,

    override val lock: Int = 0,

    val title: String? = null,

    val email: String? = null,

    override val avatar: String? = null,

    override var role: String? = "guest",

    // google auth key
    override val authKey: String? = null,

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    @Version
    val version: Long = 0,
): ExpandAccount