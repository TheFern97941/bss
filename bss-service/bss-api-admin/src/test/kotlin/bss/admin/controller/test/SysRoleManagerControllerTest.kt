package bss.admin.controller.test

import bss.admin.controller.BaseControllerTest
import bss.core.page.PageQuery
import bss.service.entity.sys.LoginLog
import bss.service.entity.sys.SysModule
import bss.service.entity.sys.SysRole
import bss.service.entity.test.TestDb
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SysRoleManagerControllerTest {

    protected val port = 8000
    protected val userId = ObjectId("677e86082c18880077365916")
    protected val token = "rcDxCbI9FNNC5hiOWTSniXp8Gh1ovumj"
    protected val tokenHeaderName = "Authorization"

    val fmtDate      = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val fmtDateTime  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val javaTimeModule = JavaTimeModule().apply {
        // LocalDate <-> "yyyy-MM-dd"
        addSerializer( LocalDate::class.java,       LocalDateSerializer(fmtDate))
        addDeserializer( LocalDate::class.java,     LocalDateDeserializer(fmtDate))
        // LocalDateTime <-> "yyyy-MM-dd HH:mm:ss"
        addSerializer( LocalDateTime::class.java,   LocalDateTimeSerializer(fmtDateTime))
        addDeserializer( LocalDateTime::class.java, LocalDateTimeDeserializer(fmtDateTime))
    }

    val objectMapper = ObjectMapper()
        // 支持 Kotlin 参数名反序列化
        .registerModule(
            KotlinModule.Builder().withReflectionCacheSize(512).configure(KotlinFeature.NullToEmptyCollection, true)
            .configure(KotlinFeature.NullToEmptyMap, true).configure(KotlinFeature.NullIsSameAsDefault, enabled = true)
            .build()
        )
        // 支持 JSR-310（LocalDate, LocalDateTime）
        .registerModule(javaTimeModule)
        // 如果要 (反)序列化 MongoDB 的 ObjectId
        // 不要把日期写成 timestamp
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        // 忽略多余字段
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    var webClient: WebTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:$port")
        .defaultHeader(tokenHeaderName, token)
        .build()

    val path = "/testDb"

    @Test
    fun page() {
        val pq = PageQuery()

        webClient.post()
            .uri(path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(objectMapper.writeValueAsString(pq))
            .exchange()
            .expectStatus().isOk
            .expectBody<Result<Page<LoginLog>>>()
            .consumeWith {
                println("[page] body: $it")
            }
    }

    @Test
    fun findById() {
        val id = "67b83d47a3403015759d36e8";
        webClient.get()
            .uri("$path/${id}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<Result<SysModule>>()
            .consumeWith {
                println("[findById] body: $it")
            }
    }

    @Test
    fun update() {
        val data = "{\"id\":\"67b83d47a3403015759d36e8\",\"name\":\"Seth Daniel DVM222\",\"age\":5,\"amount\":\"4\",\"createDay\":\"2025-02-20\",\"status\":\"NORMAL\",\"imgUrl\":\"https://picsum.photos/300/200\",\"markdownContent\":\"markdown content\",\"fullContent\":\"full content\",\"createdAt\":\"2025-02-21 16:45:59\",\"updatedAt\":\"2025-02-21 16:45:59\",\"version\":1}";
        val testDb = objectMapper.readValue(data, TestDb::class.java)
        webClient.post()
            .uri("$path/${testDb.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(objectMapper.writeValueAsString(testDb))
            .exchange()
            .expectBody<String>()
            .consumeWith {
                println("[update] body: $it")
            }
    }

//
//    @Test
//    fun delete() {
//        val id = "test22"
//        super.delete(id)
//            .expectBody<String>()
//            .consumeWith {
//                println("[delete] body: $it")
//            }
//    }
//

//
//    @Test
//    fun create() {
//        val sysModule = SysRole(id="test22", name = "test22", moduleId = Collections.emptySet<String>().toHashSet());
//        super.create(sysModule)
//            .expectBody<String>()
//            .consumeWith {
//                println("[create] body: $it")
//            }
//    }
}