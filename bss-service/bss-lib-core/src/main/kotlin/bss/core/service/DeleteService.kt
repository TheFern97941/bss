package bss.core.service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.Serializable

interface DeleteService<T : Any, ID : Serializable, MODEL : Any> : ReadOnlyService<T, ID, MODEL>{
    fun delete(ids: Array<ID>): Flux<MODEL>
    fun delete(id: ID): Mono<MODEL>
}