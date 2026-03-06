package bss.core.page

import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.hibernate.validator.constraints.Range
import org.springframework.core.convert.ConversionService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.query.Criteria
import java.time.LocalDateTime
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.javaType

fun Map<String, Any?>.toCriteria(): Criteria {
    return toCriteria { _, v -> v }
}

fun <T : Any> Map<String, Any?>.toCriteria(type: KClass<T>, conversionService: ConversionService): Criteria {
    return toCriteria({ k, v -> to(type, conversionService, k, v) })
}

@OptIn(ExperimentalStdlibApi::class)
fun <T : Any> to(type: KClass<T>, conversionService: ConversionService, k: String, v: Any?): Any? {
    if (v is String) {
        //需要按照CClass 进行转换
        val property = type.memberProperties.find { it.name == k }
        if (property != null) {
            val javaType = property.returnType.javaType as Class<*>
            if (conversionService.canConvert(v.javaClass, javaType)) {
                return conversionService.convert(v, javaType) ?: v
            }
        }
    }
    return v;
}

fun Map<String, Any?>.toCriteria(transform: (k: String, v: Any?) -> Any?): Criteria {
    val criteria = Criteria()
    for ((k, v) in this) {
        if (v is Collection<*>) {
            criteria.and(k).`in`(transform(k, v))
        } else if (v is String && ObjectId.isValid(v)) {
            criteria.and(k).`is`(ObjectId(v))
        } else {
            criteria.and(k).`is`(transform(k, v))
        }
    }
    return criteria
}

data class PageQuery(
    val current: Int = 0,
    @field:Range(min = 1, max = 1000)
    val pageSize: Int = 20,
    val sort: LinkedHashMap<String, Sort.Direction>? = LinkedHashMap(),
    val filter: LinkedHashMap<String, Any?> = LinkedHashMap(),
) {
    fun <T : Any> toCriteria(type: KClass<T>, conversionService: ConversionService): Criteria {

        val baseCriteria = filter.filter { it.key != "createdStartTime" && it.key != "createdEndTime" }
            .toCriteria(type, conversionService)

        val createTimeRange = filter.keys.any { it == "createdStartTime" || it == "createdEndTime" }

        // 后面优化成 一个复用代码, 就这样搞! 针对 date range的
        return if (createTimeRange) {
            val timeCriteria = Criteria("createdDate")
                .apply {
                    filter["createdStartTime"]?.let {
                        conversionService.convert(it, LocalDateTime::class.java)?.let { td ->
                            gte(td)
                        }
                    }
                    filter["createdEndTime"]?.let {
                        conversionService.convert(it, LocalDateTime::class.java)?.let { td ->
                            lte(td)
                        }
                    }
                }
            Criteria().andOperator(baseCriteria, timeCriteria)
        } else {
            baseCriteria
        }
    }

    fun getFilter(field: String): Any? {
        return filter.get(field)
    }

    fun toSort(): List<Sort> {
        return sort?.map { (k, v) -> Sort.by(v, k) } ?: emptyList()
    }

    fun toPageable(): PageRequest {
        return PageRequest.of(current, pageSize)
    }

    fun toPageable(vararg properties: String): PageRequest {
        return PageRequest.of(current, pageSize)
    }
}