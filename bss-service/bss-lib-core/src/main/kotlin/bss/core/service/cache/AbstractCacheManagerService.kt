package bss.core.service

import bss.core.exception.MessageException
import bss.core.page.PageReactiveMongoRepository
import bss.core.utils.DataClassUtils
import org.springframework.dao.DuplicateKeyException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toMono
import java.io.Serializable
import java.util.concurrent.atomic.AtomicReference

abstract class AbstractCacheManagerService<T : Any, K : Serializable, EDIT : Any, CREATE : Any, MODEL : Any>(
    repository: PageReactiveMongoRepository<T, K>
) : AbstractCacheReadOnlyService<T, K, MODEL>(repository), ManagerService<T, K, EDIT, CREATE, MODEL> {


    override fun create(form: CREATE): Mono<MODEL> {
        return this.handleCreate(form)
            .publishOn(Schedulers.boundedElastic())
            .flatMap { insert(it) }
            .doOnNext(this::fireChangeEvent)
            .doOnNext(this::onCreateSuccess)
            .onErrorMap { handleCreateError(it) }
            .flatMap { toModel(it) }
    }

    open fun onCreateSuccess(t: T) {

    }

    open fun insert(it: T) = repository.insert(it)

    open fun handleCreate(form: CREATE, vararg pairs: Pair<String, Any>): Mono<T> {
        return DataClassUtils.new(cls, form, *pairs).toMono()
    }

    open fun handleCreateError(it: Throwable): Throwable {
        return when (it::class) {
            DuplicateKeyException::class -> handleDuplicateKeyError(it)
            else -> it
        }
    }

    override fun update(id: K, form: EDIT): Mono<MODEL> {
        val oldRef = AtomicReference<T>()
        return this.repositoryFindById(id)
            .switchIfEmpty(Mono.defer {
                MessageException(
                    "notFound",
                    "Not Found ${id}"
                ).toMono()
            })
            .doOnNext(oldRef::set)
            .publishOn(Schedulers.boundedElastic())
            .flatMap { this.handleUpdate(id, form, it) }
            .flatMap { doUpdate(it) }
            .doOnNext(this::fireChangeEvent)
            .doOnNext { this.onUpdateSuccess(oldRef.get(), it) }
            .flatMap { toModel(it) }
    }

    open fun handleUpdate(id: K, form: EDIT, t: T, vararg pairs: Pair<String, Any>): Mono<T> =
        DataClassUtils.copy<T>(form, t, *pairs).toMono()

    open fun doUpdate(it: T) = repository.save(it)


    open fun onUpdateSuccess(old: T, newObj: T) {

    }

    open fun handleDuplicateKeyError(it: Throwable) = MessageException("duplicate", "Adding duplicate entry", it)

    open fun onDeleteSuccess(t: T) {

    }

    override fun delete(ids: Array<K>): Flux<MODEL> {
        return Flux.fromArray(ids)
            .flatMap { delete(it) }
    }

    override fun delete(id: K): Mono<MODEL> {
        val t = super.findByIdNotNull(id)
        return repository.deleteById(id)
            .then(toModel(t))
            .doFinally {
                this.fireChangeEventById(
                    id
                )
                onDeleteSuccess(t)
            }
    }
}