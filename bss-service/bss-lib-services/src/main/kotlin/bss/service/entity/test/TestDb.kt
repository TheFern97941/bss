package bss.service.entity.test

import bss.service.enums.StatusEnums
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Document
data class TestDb (
    @Id
    @Field(targetType = FieldType.OBJECT_ID)
    val id: ObjectId = ObjectId.get(),

    @Indexed
    val name: String,

    val age: Int,

    @Field(targetType = FieldType.DECIMAL128)
    val amount: BigDecimal,

    val createDay: LocalDate = LocalDate.now(),

    val status: StatusEnums = StatusEnums.NORMAL,

    val imgUrl: String = "https://picsum.photos/300/200",

    val markdownContent: String = "markdown content",

    val fullContent: String = "full content",

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    @Version
    val version: Long = 0,
)