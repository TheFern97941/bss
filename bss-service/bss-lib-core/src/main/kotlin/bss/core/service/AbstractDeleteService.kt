package bss.core.service

import bss.core.page.PageReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.Serializable

abstract class AbstractDeleteService<T : Any, ID : Serializable, MODEL : Any>(
    repository: PageReactiveMongoRepository<T, ID>,
) : AbstractReadOnlyService<T, ID, MODEL>(repository), DeleteService<T, ID, MODEL> {

    override fun delete(ids: Array<ID>): Flux<MODEL> {
        // 从数据库一次拿出来
        return repository.findAllById(ids.asList())
            .flatMap { t ->
                doDelete(t)
                    .doOnSuccess { this.onDeleteSuccess(t) }
                    .then(doTransform(t))
            }
    }

    override fun delete(id: ID): Mono<MODEL> {
        return repository.findById(id)
            .flatMap { m ->
                doDelete(m)
                    .doOnSubscribe { this.onDeleteSuccess(m) }
                    .then(doTransform(m))
            }

    }

    open fun doDelete(t: T): Mono<Void> {
        return repository.delete(t)
    }

    open fun onDeleteSuccess(t: T) {

    }
}