package bss.core.service

import bss.core.exception.MessageException
import bss.core.page.PageQuery
import bss.core.page.PageReactiveMongoRepository
import bss.core.utils.DataClassUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import java.io.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.jvmErasure

abstract class AbstractReadOnlyService<T : Any, ID : Serializable, MODEL : Any>(
     protected val repository: PageReactiveMongoRepository<T, ID>,
) : ReadOnlyService<T, ID, MODEL> {

    private var notTransform: Boolean
    private var modelCls: KClass<MODEL>

    init {
        // 获取直接的父类
        val type = this::class.allSupertypes.first {
            it.classifier == AbstractReadOnlyService::class
        }

        // 提取类型参数
        val typeArgs = type.arguments
        if (typeArgs.size < 3) throw IllegalStateException("Expected at least 3 type arguments")

        val tType = typeArgs[0].type ?: throw IllegalStateException("Cannot determine type T")
        val modelType = typeArgs[2].type ?: throw IllegalStateException("Cannot determine type MODEL")

        @Suppress("UNCHECKED_CAST")
        modelCls = modelType.classifier as KClass<MODEL>

        notTransform = tType.jvmErasure == modelType.jvmErasure
    }

    override fun findPage(form: PageQuery): Mono<Page<MODEL>> {
        return repository.findPage(form)
            .flatMap(this::transform)
    }

    override fun findByIdToModel(id: ID): Mono<MODEL> {
        return findById(id)
            .switchIfEmpty { MessageException("notFound", "未找到:${id}").toMono() }
            .flatMap(this::doTransform)
    }

    open fun findById(id: ID) = repository.findById(id)
        .switchIfEmpty { MessageException("notFound", "未找到:${id}").toMono() }

    override fun findAllById(ids: Array<ID>): Flux<MODEL> {
        return repository.findAllById(ids.asList())
            .flatMap(this::transform)
    }

    open fun transform(page: Page<T>): Mono<Page<MODEL>> {
        if (notTransform) {
            @Suppress("UNCHECKED_CAST")
            return Mono.just(page).map { it as Page<MODEL> }
        } else {
            return Flux.fromIterable(page.content)
                .flatMapSequential(this::transform)
                .collectList()
                .map { PageImpl(it, page.pageable, page.totalElements) }
        }
    }

    open fun doTransform(t: T): Mono<MODEL> {
        if (notTransform) {
            println("exec doTransform .... ")
            @Suppress("UNCHECKED_CAST")
            return (t as MODEL).toMono()
        } else {
            return transform(t)
        }
    }

    // 如果 T 和 MODEL不同, 这个方法就是用来覆盖的,
    // 除非 T 的数据已经够用了
    open fun transform(t: T): Mono<MODEL> = DataClassUtils.new(modelCls, t).toMono()
}