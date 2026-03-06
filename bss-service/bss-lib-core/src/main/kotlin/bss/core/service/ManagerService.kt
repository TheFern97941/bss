package bss.core.service

import reactor.core.publisher.Mono
import java.io.Serializable

interface ManagerService<T : Any, ID : Serializable, EDIT : Any, CREATE : Any, MODEL : Any> : DeleteService<T, ID, MODEL> {

    fun update(id: ID, form: EDIT): Mono<MODEL>

    fun create(form: CREATE): Mono<MODEL>

}