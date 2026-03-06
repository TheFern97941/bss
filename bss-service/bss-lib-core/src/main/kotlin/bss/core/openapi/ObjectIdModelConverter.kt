package bss.core.openapi

import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverter
import io.swagger.v3.core.converter.ModelConverterContext
import io.swagger.v3.oas.models.media.Schema
import org.bson.types.ObjectId
import org.springdoc.core.providers.ObjectMapperProvider

class ObjectIdModelConverter(
    private val springDocObjectMapper: ObjectMapperProvider
) : ModelConverter {

    override fun resolve(
        type: AnnotatedType,
        context: ModelConverterContext,
        chain: Iterator<ModelConverter>
    ): Schema<*>? {
        val javaType = springDocObjectMapper.jsonMapper().constructType(type.type)
        val cls = javaType.rawClass

        // 捕捉 org.bson.types.ObjectId 类型
        if (ObjectId::class.java.isAssignableFrom(cls)) {
            // 构造 AnnotatedType -> String.class
            val stringType = AnnotatedType(String::class.java)
                .ctxAnnotations(type.ctxAnnotations)
                .jsonViewAnnotation(type.jsonViewAnnotation)
                .resolveAsRef(false) // 👈 重点：不要 $ref，直接 inline 展开
            return context.resolve(stringType)
        }

        return if (chain.hasNext()) chain.next().resolve(type, context, chain) else null
    }
}