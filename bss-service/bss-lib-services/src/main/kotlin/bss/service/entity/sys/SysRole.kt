package bss.service.entity.sys

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.IndexDirection.DESCENDING
import java.time.LocalDateTime
import java.util.HashSet

@Document
data class SysRole(
    /**
     * 名称
     */
    @Id
    val id: String,
    val name: String,
    var moduleId: HashSet<String>,

    @CreatedDate
    @Indexed(direction = DESCENDING)
    val createdAt: LocalDateTime? = null,
    @LastModifiedDate
    val updatedAt: LocalDateTime? = null
)