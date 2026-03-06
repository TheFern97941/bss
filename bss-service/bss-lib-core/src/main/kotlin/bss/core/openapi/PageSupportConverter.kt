package bss.core.openapi

import com.fasterxml.jackson.databind.JavaType
import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverter
import io.swagger.v3.core.converter.ModelConverterContext
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.media.StringSchema
import org.apache.commons.lang3.reflect.TypeUtils
import org.springdoc.core.providers.ObjectMapperProvider
import org.springframework.data.domain.Page
import bss.core.openapi.Page as MyPage


class PageSupportConverter(
    private val springDocObjectMapper: ObjectMapperProvider,
) : ModelConverter {

    override fun resolve(
        type: AnnotatedType,
        context: ModelConverterContext,
        chain: Iterator<ModelConverter>,
    ): Schema<out Any>? {
        val javaType = springDocObjectMapper.jsonMapper().constructType(type.getType())
        if (javaType != null) {
            val cls = javaType.rawClass
            if (Page::class.java.isAssignableFrom(cls)) {
                val innerType: JavaType = javaType.getBindings().getBoundType(0) ?: return StringSchema()
                val parameterizedType = TypeUtils.parameterize(MyPage::class.java, innerType)
                val myPage = springDocObjectMapper.jsonMapper().constructType(parameterizedType)
                val resolve = context.resolve(
                    AnnotatedType(myPage)
                        .jsonViewAnnotation(type.getJsonViewAnnotation())
                        .ctxAnnotations((type.getCtxAnnotations()))
                        .resolveAsRef(true)
                )
                return resolve
            }
        }
        return if (chain.hasNext()) {
            chain.next().resolve(type, context, chain)
        } else {
            null
        }
    }
}