package bss.core.controller

import bss.core.annotation.SecurityAnnotation
import bss.core.service.ManagerService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Mono
import java.io.Serializable

abstract class AbstractManagerController<T : Any, ID : Serializable, EDIT : Any, CREATE : Any, MODEL : Any>(
    private val managerService: ManagerService<T, ID, EDIT, CREATE, MODEL>,
) : AbstractManagerDeleteController<T, ID, MODEL>(managerService) {


    @SecurityAnnotation("*.update")
    @PostMapping("{id}")
    fun update(
        @PathVariable id: ID,
        @Valid @RequestBody form: EDIT
    ): Mono<MODEL> {
        return managerService.update(id, form)
    }

    @SecurityAnnotation("*.create")
    @PostMapping("create")
    fun create(
        @Valid @RequestBody form: CREATE
    ): Mono<MODEL> {
        return managerService.create(form)
    }
}