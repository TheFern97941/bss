package bss.core.controller

import bss.core.annotation.SecurityAnnotation
import bss.core.page.PageQuery
import bss.core.service.ReadOnlyService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.Serializable

abstract class AbstractManagerReadOnlyController<T : Any, ID : Serializable, MODEL : Any>(
    private val managerService: ReadOnlyService<T, ID, MODEL>,
) {

    @SecurityAnnotation("*.show")
    @PostMapping("page")
    open fun findPage(
        @Valid
        @RequestBody
        form: PageQuery
    ): Mono<Page<MODEL>> {
        return managerService.findPage(form)
    }

    @SecurityAnnotation("*.show")
    @GetMapping("/{id}")
    open fun findById(
        @PathVariable
        id: ID
    ): Mono<MODEL> {
        return managerService.findByIdToModel(id)
    }

    @SecurityAnnotation("*.show")
    @GetMapping("/all/{ids}")
    open fun findAllById(
        @PathVariable
        ids: Array<ID>
    ): Flux<MODEL> {
        return managerService.findAllById(ids)
    }
}