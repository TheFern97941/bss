package bss.core.utils

import bss.core.exception.MessageException
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.core.convert.ConversionService
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*


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

    fun <T> copy(src: Any, dest: Any, vararg pairs: Pair<String, Any>): T {
        return doCopy(src, dest, arrayOf(*pairs))
    }

    fun <T> copy(src: Any, dest: Any, ignoreProperties: Set<String>): T {
        return doCopy(src, dest, emptyArray(), ignoreProperties)
    }

    private fun <T> doCopy(
        src: Any,
        dest: Any,
        pairs: Array<Pair<String, Any>>,
        ignoreProperties: Set<String>? = null,
    ): T {
        val copyFun = dest::class.memberFunctions.first { it.name == "copy" }
        val paramMap = copyFun.parameters.associateBy { it.name }

        val args: HashMap<KParameter, Any?> = HashMap()
        val pairsMap = mapOf(*pairs)

        val instanceParam = copyFun.instanceParameter!!

        args[instanceParam] = dest
        src::class.memberProperties.forEach { srcMemberProperty ->
            if (ignoreProperties?.contains(srcMemberProperty.name) == true) {
                return@forEach
            }

            val key = paramMap[srcMemberProperty.name]
            if (key != null) {
                var value = pairsMap[srcMemberProperty.name]
                if (value == null) {
                    value = srcMemberProperty.call(src)
                    if (value == null && !key.type.isMarkedNullable) {
                        throw MessageException(
                            "requiredParameterIsMissingValue",
                            "required parameter [${srcMemberProperty.name}] is missing a value"
                        )
                    }
                }
                if (value == null && key.type.isMarkedNullable) {
                    args.setValue(key, null)
                } else {
                    value?.let {v -> args.setValue(key, v) }
                }
            }
        }

        return copyFun.callBy(args) as T
    }


    fun <T : Any> new(kClass: KClass<T>, src: Any, vararg pairs: Pair<String, Any?>): T {
        val constructor = kClass.primaryConstructor!!

        val srcPropertiesMap = src::class.memberProperties
            .filter { it.getter.findAnnotation<JsonIgnore>() == null }
            .associateBy({ it.name }, { it.call(src) })

        val args: HashMap<KParameter, Any?> = HashMap()

        val pairsMap = mapOf(*pairs)

        constructor.parameters.forEach { param ->
            var value = pairsMap[param.name]
            var continueSet = false;
            if (value == null) {
                value = srcPropertiesMap[param.name]
                if (value == null) {
                    // 有默认值 就用默认值
                    if (param.isOptional) {
                        continueSet = true
                    } else if (param.type.isMarkedNullable) {
                        // 允许为空, 就设置空

                    } else {
                        throw MessageException(
                            "requiredParameterIsMissingValue"
                            , "required parameter [${param.name}] is missing a value"
                        )
                    }
                }
            }
            if (!continueSet) {
                args.setValue(param, value)
            }
        }
        return constructor.callBy(args)
    }

    private fun HashMap<KParameter, Any?>.setValue(
        param: KParameter,
        srcValue: Any?,
    ) {
        this[param] = srcValue
    }
}