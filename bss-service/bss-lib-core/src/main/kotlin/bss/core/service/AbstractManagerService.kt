package bss.core.service

import bss.core.exception.MessageException
import bss.core.page.PageReactiveMongoRepository
import bss.core.utils.DataClassUtils
import org.springframework.dao.DuplicateKeyException
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toMono
import java.io.Serializable
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes

abstract class AbstractManagerService<T : Any, ID : Serializable, EDIT : Any, CREATE : Any, MODEL : Any>(
    repository: PageReactiveMongoRepository<T, ID>,
) : AbstractDeleteService<T, ID, MODEL>(repository), ManagerService<T, ID, EDIT, CREATE, MODEL> {

    override fun create(form: CREATE): Mono<MODEL> {
        val reference = AtomicReference<T>()
        return this.handleCreate(form)
            .doOnNext(reference::set)
            .publishOn(Schedulers.boundedElastic())
            .flatMap { insert(it) }
            .doOnNext(this::onCreateSuccess)
            .onErrorResume { handleCreateError(it, reference.get()) }
            .flatMap(this::doTransform)
    }

    open fun insert(it: T) = repository.insert(it)

    open fun onCreateSuccess(t: T) {

    }

    open fun handleCreate(form: CREATE, vararg pairs: Pair<String, Any>): Mono<T> {
        val type = this::class.allSupertypes.first {
            it.classifier == AbstractManagerService::class
        }

        val classifier = type.arguments[0].type?.classifier ?: return MessageException(
            "genericParameterError", "Generic parameter error"
        ).toMono()

        @Suppress("UNCHECKED_CAST")
        val kClass = classifier as KClass<T>
        return DataClassUtils.new(kClass, form, *pairs).toMono()
    }

    open fun handleCreateError(it: Throwable, t: T): Mono<T> {
        return when (it::class) {
            DuplicateKeyException::class -> handleDuplicateKeyError(it, t)
            else -> Mono.error(it)
        }
    }

    open fun handleDuplicateKeyError(it: Throwable, t: T): Mono<T> {
        return Mono.error(MessageException("duplicate", "Adding duplicate entry:${t}", it))
    }


    override fun update(id: ID, form: EDIT): Mono<MODEL> {
        val oldRef = AtomicReference<T>()
        return findById(id).doOnNext(oldRef::set)
            .publishOn(Schedulers.boundedElastic())
            .flatMap { this.handleUpdate(id, form, it) }
            .flatMap { doUpdate(it) }
            .doOnNext { this.onUpdateSuccess(oldRef.get(), it) }
            .flatMap(this::doTransform)
    }

    open fun doUpdate(it: T) = repository.save(it)

    open fun onUpdateSuccess(old: T, newObj: T) {

    }

    open fun handleUpdate(id: ID, form: EDIT, t: T, vararg pairs: Pair<String, Any>): Mono<T> =
        DataClassUtils.copy<T>(form, t, *pairs).toMono()
}