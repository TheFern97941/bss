package bss.core.utils

import bss.core.exception.MessageException
import org.springframework.core.convert.ConversionService
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure


object DataClassUtils {
    private val EMPTY_FORM = BodyInserters.fromFormData(LinkedMultiValueMap())

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> toForm(
        conversionService: ConversionService, data: T?
    ): BodyInserters.FormInserter<String> {
        if (data == null) {
            return EMPTY_FORM
        }
        val map = LinkedMultiValueMap<String, String>()
        data::class.memberProperties.forEach {
            val value = (it as KProperty1<T, *>).get(data)
            map.add(it.name, conversionService.convert(value, String::class.java))
        }
        return BodyInserters.fromFormData(map)
    }


    @Suppress("UNCHECKED_CAST")
    fun <T : Any, V> getPropertyValue(property: String, data: T): V {
        val formProperty: KProperty1<T, V> =
            data::class.memberProperties.find { r -> r.name == property } as KProperty1<T, V>

        return formProperty.get(data)
    }

    fun <T : Any> copy(
        src: Any,
        dest: T,
        vararg pairs: Pair<String, Any?>,
        ignoreProperties: Set<String> = emptySet(),
        conversionService: ConversionService? = null
    ): T {
        return doCopy(src, dest, pairs, ignoreProperties, conversionService)
    }

    private fun <T : Any> doCopy(
        src: Any,
        dest: T,
        pairs: Array<out Pair<String, Any?>>,
        ignoreProperties: Set<String> = emptySet(),
        conversionService: ConversionService? = null,
    ): T {
        val copyFun = dest::class.memberFunctions.first { it.name == "copy" }
        val pairsMap = mapOf(*pairs)
        val srcPropertiesMap: Map<String, KProperty1<out Any, *>> = src::class.memberProperties.associateBy { it.name }

        val args: HashMap<KParameter, Any?> = HashMap()
        val instanceParam = copyFun.instanceParameter!!
        args[instanceParam] = dest

        copyFun.parameters.forEach { param ->
            if (param.kind == KParameter.Kind.INSTANCE) return@forEach
            val paramName = param.name ?: return@forEach
            if (ignoreProperties.contains(paramName)) return@forEach

            if (pairsMap.containsKey(paramName)) {
                args[param] = convertValue(pairsMap[paramName], param.type, conversionService)
                return@forEach
            }

            val srcProp = srcPropertiesMap[paramName]
            if (srcProp != null) {
                val srcValue = srcProp.call(src)
                when {
                    srcValue != null -> args[param] = convertValue(srcValue, param.type, conversionService)
                    param.type.isMarkedNullable -> args[param] = null
                    // srcValue == null && not nullable → skip, callBy uses dest's current value
                }
            }
            // src doesn't have this property → skip, preserve dest's current value
        }

        @Suppress("UNCHECKED_CAST")
        return copyFun.callBy(args) as T
    }


    fun <T : Any> new(
        kClass: KClass<T>,
        src: Any,
        vararg pairs: Pair<String, Any?>,
        ignoreProperties: Set<String> = emptySet(),
        conversionService: ConversionService? = null
    ): T {
        val constructor = kClass.primaryConstructor!!
        val srcPropertiesMap: Map<String, KProperty1<out Any, *>> = src::class.memberProperties.associateBy { it.name }

        val args: HashMap<KParameter, Any?> = HashMap()
        val pairsMap = mapOf(*pairs)

        constructor.parameters.forEach { param ->
            val paramName = param.name ?: return@forEach
            if (ignoreProperties.contains(paramName)) return@forEach

            if (pairsMap.containsKey(paramName)) {
                args[param] = convertValue(pairsMap[paramName], param.type, conversionService)
                return@forEach
            }

            val srcProp = srcPropertiesMap[paramName]
            if (srcProp != null) {
                val srcValue = srcProp.call(src)
                when {
                    srcValue != null -> args[param] = convertValue(srcValue, param.type, conversionService)
                    param.isOptional -> { /* skip, use declared default value */ }
                    param.type.isMarkedNullable -> args[param] = null
                    else -> throw MessageException(
                        "requiredParameterIsMissingValue",
                        "required parameter [$paramName] is missing a value"
                    )
                }
            } else {
                when {
                    param.isOptional -> { /* skip, use declared default value */ }
                    param.type.isMarkedNullable -> args[param] = null
                    else -> throw MessageException(
                        "requiredParameterIsMissingValue",
                        "required parameter [$paramName] is missing a value"
                    )
                }
            }
        }
        return constructor.callBy(args)
    }

    private fun convertValue(
        value: Any?,
        targetType: KType,
        conversionService: ConversionService? = null
    ): Any? {
        if (value == null) return null
        val targetKClass = targetType.jvmErasure
        // Already matching type
        if (targetKClass.isInstance(value)) return value
        // ConversionService
        if (conversionService?.canConvert(value::class.java, targetKClass.java) == true)
            return conversionService.convert(value, targetKClass.java)
        // Number conversions
        if (value is Number) return when (targetKClass) {
            Int::class     -> value.toInt()
            Long::class    -> value.toLong()
            Double::class  -> value.toDouble()
            Float::class   -> value.toFloat()
            Short::class   -> value.toShort()
            Byte::class    -> value.toByte()
            String::class  -> value.toString()
            else           -> value
        }
        // String to Number/Boolean conversions
        if (value is String) return when (targetKClass) {
            Int::class     -> value.toIntOrNull()             ?: value
            Long::class    -> value.toLongOrNull()            ?: value
            Double::class  -> value.toDoubleOrNull()          ?: value
            Float::class   -> value.toFloatOrNull()           ?: value
            Boolean::class -> value.toBooleanStrictOrNull()   ?: value
            else           -> value
        }
        return value
    }
}
