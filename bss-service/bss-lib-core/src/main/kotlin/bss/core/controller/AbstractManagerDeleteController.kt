package bss.core.controller

import bss.core.annotation.SecurityAnnotation
import bss.core.service.DeleteService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.Serializable

abstract class AbstractManagerDeleteController<T : Any, ID : Serializable, MODEL : Any>(
    private val managerService: DeleteService<T, ID, MODEL>,
) : AbstractManagerReadOnlyController<T, ID, MODEL>(managerService) {


    @SecurityAnnotation("*.delete")
    @DeleteMapping("deleteAll/{id}")
    fun delete(@PathVariable id: Array<ID>): Flux<MODEL> {
        return managerService.delete(id)
    }

    @SecurityAnnotation("*.delete")
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: ID): Mono<MODEL> {
        return managerService.delete(id)
    }
}