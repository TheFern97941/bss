package bss.core.service

import bss.core.page.PageReactiveMongoRepository
import bss.core.service.cache.EntityChangeService
import org.springframework.beans.factory.annotation.Autowired
import java.io.Serializable
import kotlin.reflect.KClass

abstract class AbstractCacheBroadcastService<T : Any, ID : Serializable, EDIT : Any, CREATE : Any, MODEL : Any>(
    repository: PageReactiveMongoRepository<T, ID>,
    private val cls: KClass<T>
) : AbstractManagerService<T, ID, EDIT, CREATE, MODEL>(repository) {

    abstract fun getId(t: T): ID

    @Autowired
    lateinit var entityChangeService: EntityChangeService<ID>

    override fun onDeleteSuccess(t: T) {
        entityChangeService.fireChangeEvent(cls, getId(t))
        super.onDeleteSuccess(t)
    }

    override fun onCreateSuccess(t: T) {
        entityChangeService.fireChangeEvent(cls, getId(t))
        super.onCreateSuccess(t)
    }

    override fun onUpdateSuccess(old: T, newObj: T) {
        entityChangeService.fireChangeEvent(cls, getId(newObj))
        super.onUpdateSuccess(old, newObj)
    }
}