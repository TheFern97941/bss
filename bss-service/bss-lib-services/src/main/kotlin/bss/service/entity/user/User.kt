package bss.service.entity.user

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
data class User(
    @Id
    override val id: ObjectId = ObjectId.get(),

    @Indexed(unique = true)
    val uid: Int,

    @Indexed(unique = true)
    override val username: String,

    override val password: String,

    var inviteCode: String,

    val name: String = "",

    override val lock: Int = 0,

    // google auth key
    override val authKey: String?,

    @Indexed
    var status: StatusEnums = StatusEnums.NORMAL,

    @Indexed(unique = true, sparse = true)
    var email: String? = null,

    @Indexed(unique = true, sparse = true)
    var phone: String? = null,

    val desc: String? = null,

    override val avatar: String? = null,

    val regDeviceId: String? = null,

    override val locale: Locales = Locales.DEFAULT,

    @Indexed
    val parentUserId: ObjectId? = null,

    /**
     * 是否是代理
     */
    val isAgent: Boolean = false,

    val country: String? = null,

    val countryCode: String? = null,

    val createAt: ObjectId? = null,

    override val role: String? = null,

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    @Version
    val version: Long = 0,
) : ExpandAccount
