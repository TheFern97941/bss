package bss.core

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.*
import org.apache.commons.lang3.time.DurationFormatUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jackson.JsonComponent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.io.IOException
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

@Configuration
class JsonComponentConfiguration {

    @Value("\${spring.webflux.format.date-time}")
    private val dateTime: String? = null

    @Value("\${spring.webflux.format.date}")
    private val date: String? = null

    @Value("\${spring.webflux.format.time}")
    private val time: String? = null

    @Bean
    fun dateTimeFormatter(): DateTimeFormatter {
        return DateTimeFormatter.ofPattern(dateTime)
    }

    @Bean
    fun dateFormatter(): DateTimeFormatter {
        return DateTimeFormatter.ofPattern(date)
    }

    @Bean
    fun timeFormatter(): DateTimeFormatter {
        return DateTimeFormatter.ofPattern(time)
    }

    @JsonComponent
    class MyBigDecimalDeserializer() : JsonDeserializer<BigDecimal>() {
        override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext?): BigDecimal {
            val codec = jsonParser.codec
            val value = codec.readValue(jsonParser, String::class.java)
            return BigDecimal(value)
        }
    }

    @JsonComponent
    class MyBigDecimalSerializer() : JsonSerializer<BigDecimal>() {
        @Throws(IOException::class)
        override fun serialize(value: BigDecimal, jgen: JsonGenerator, serializers: SerializerProvider) {
            jgen.writeString(value.toString())
        }
    }

    @JsonComponent
    class MyDurationDeserializer() : JsonDeserializer<Duration>() {
        override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext?): Duration {
            val codec = jsonParser.codec
            val value = codec.readValue(jsonParser, String::class.java)
            return Duration.parse(value)
        }
    }

    @JsonComponent
    class MyDurationSerializer() : JsonSerializer<Duration>() {
        @Throws(IOException::class)
        override fun serialize(value: Duration, jgen: JsonGenerator, serializers: SerializerProvider) {
            jgen.writeString(DurationFormatUtils.formatDuration(value.toMillis(), "HH:mm:ss"))
        }
    }

    @JsonComponent
    class MyInstantDeserializer(dateTimeFormatter: DateTimeFormatter) :
        InstantDeserializer<Instant>(INSTANT, dateTimeFormatter)

    @JsonComponent
    class MyInstantSerializer(dateTimeFormatter: DateTimeFormatter) :
        InstantSerializer(INSTANCE, false, false, dateTimeFormatter)

    @JsonComponent
    class MyOffsetDateTimeDeserializer(dateTimeFormatter: DateTimeFormatter) :
        InstantDeserializer<OffsetDateTime>(OFFSET_DATE_TIME, dateTimeFormatter)

    @JsonComponent
    class MyOffsetDateTimeSerializer(dateTimeFormatter: DateTimeFormatter) :
        OffsetDateTimeSerializer(INSTANCE, false, false, dateTimeFormatter)

    @JsonComponent
    class MyZonedDateTimeDeserializer(dateTimeFormatter: DateTimeFormatter) :
        InstantDeserializer<ZonedDateTime>(ZONED_DATE_TIME, dateTimeFormatter)

    @JsonComponent
    class MyZonedDateTimeSerializer(dateTimeFormatter: DateTimeFormatter) : ZonedDateTimeSerializer(dateTimeFormatter)

    @JsonComponent
    class MyLocalDateTimeDeserializer(dateTimeFormatter: DateTimeFormatter) :
        LocalDateTimeDeserializer(dateTimeFormatter)

    @JsonComponent
    class MyLocalDateTimeSerializer(dateTimeFormatter: DateTimeFormatter) : LocalDateTimeSerializer(dateTimeFormatter)

    @JsonComponent
    class MyLocalDateDeserializer(dateFormatter: DateTimeFormatter) : LocalDateDeserializer(dateFormatter)

    @JsonComponent
    class MyLocalDateSerializer(dateFormatter: DateTimeFormatter) : LocalDateSerializer(dateFormatter)

    @JsonComponent
    class MyLocalTimeDeserializer(timeFormatter: DateTimeFormatter) : LocalTimeDeserializer(timeFormatter)

    @JsonComponent
    class MyLocalTimeSerializer(timeFormatter: DateTimeFormatter) : LocalTimeSerializer(timeFormatter)


    @JsonComponent
    class ObjectIdSerializer : JsonSerializer<ObjectId>() {
        @Throws(IOException::class)
        override fun serialize(value: ObjectId, jgen: JsonGenerator, serializers: SerializerProvider) {
            jgen.writeString(value.toString())
        }
    }

    @JsonComponent
    class ObjectIdDeserializer : JsonDeserializer<ObjectId>() {
        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext): ObjectId {
            val codec = jsonParser.codec
            val value = codec.readValue(jsonParser, String::class.java)
            return ObjectId(value)
        }
    }


    @JsonComponent
    class PageableJsonSerializer : JsonSerializer<Pageable>() {
        @Throws(IOException::class)
        override fun serialize(value: Pageable, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeStartObject()
            gen.writeObjectField("sort", value.sort)
            gen.writeNumberField("current", value.pageNumber)
            gen.writeNumberField("pageSize", value.pageSize)
            gen.writeEndObject()
        }
    }

    @JsonComponent
    class SortJsonSerializer : JsonSerializer<Sort>() {
        @Throws(IOException::class)
        override fun serialize(value: Sort, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeStartObject()
            gen.writeObjectField("orders", value.stream().collect(Collectors.toList()))
            gen.writeEndObject()
        }
    }


    @JsonComponent
    class PageJsonSerializer : JsonSerializer<Page<*>>() {
        @Throws(IOException::class)
        override fun serialize(value: Page<*>, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeStartObject()
            gen.writeNumberField("pageSize", value.size)
            gen.writeNumberField("current", value.number)
            gen.writeNumberField("total", value.totalElements)
            gen.writeObjectField("data", value.content)
            gen.writeEndObject()
        }
    }
}

