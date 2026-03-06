package bss.core.service

import bss.core.page.PageQuery
import org.springframework.data.domain.Page
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.Serializable

interface ReadOnlyService<T : Any, ID : Serializable, MODEL : Any> {

    fun findPage(form: PageQuery): Mono<Page<MODEL>>

    fun findAllById(ids: Array<ID>): Flux<MODEL>

    fun findByIdToModel(id: ID): Mono<MODEL>
}