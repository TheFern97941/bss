package bss.service.entity.sys

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.IndexDirection
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class SysModule(
    @Id
    val id: String,
    val parent: String? = null,
    val path: String? = null,
    val name: String,
    @CreatedDate
    @Indexed(direction = IndexDirection.DESCENDING)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    val updatedAt: LocalDateTime? = null
)