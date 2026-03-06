package bss.service.services.sys

import bss.core.exception.MessageException
import bss.core.service.AbstractCacheManagerService
import bss.service.entity.sys.SysModule
import bss.service.repository.sys.SysModuleRepository
import bss.service.services.sys.form.SysModuleForm
import bss.service.services.sys.model.SysModuleModel
import jakarta.annotation.PostConstruct
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.atomic.AtomicReference

@Service
class SysModuleService(repository: SysModuleRepository) :
    AbstractCacheManagerService<SysModule, String, SysModuleForm, SysModuleForm, SysModuleModel>(repository) {

    private val treeCache = AtomicReference<List<SysModuleModel>>(emptyList())


    @PostConstruct
    override fun init() {
        super.init()
    }

    override fun onDelCache(t: SysModule) {

    }

    override fun onCache(t: SysModule, replace: Boolean) {

    }

    override fun onInit(list: List<SysModule>) {
        refresh(list);
    }

    override fun onDeleteSuccess(t: SysModule) {
        refresh(getAll())
    }

    override fun onUpdateSuccess(old: SysModule, newObj: SysModule) {
        refresh(getAll())
    }

    fun refresh(list: Collection<SysModule>) {
        treeCache.set(buildModuleTree(list))
    }

    fun buildModuleTree(modules: Collection<SysModule>): List<SysModuleModel> {
        val idToModelMap = mutableMapOf<String, SysModuleModel>()
        val rootModules = mutableListOf<SysModuleModel>()

        modules.forEach { module ->
            val model = SysModuleModel(
                id = module.id,
                parent = module.parent,
                path = module.path,
                name = module.name,
                createdAt = module.createdAt,
                updatedAt = module.updatedAt
            )
            idToModelMap[module.id] = model
        }

        idToModelMap.values.forEach { model ->
            if (model.parent != null && idToModelMap.containsKey(model.parent)) {
                val parent = idToModelMap[model.parent]
                parent?.apply {
                    children = (children ?: mutableListOf()).apply { add(model) }
                }
            } else {
                rootModules.add(model)
            }
        }

        return rootModules
    }


    override fun handleCreate(form: SysModuleForm, vararg pairs: Pair<String, Any>): Mono<SysModule> {
        if (form.id == form.parent)
            return MessageException("parentNotSame", "Parent can't be same as self.").toMono()
        return super.handleCreate(form, *pairs)
    }

    override fun handleUpdate(
        id: String,
        form: SysModuleForm,
        t: SysModule,
        vararg pairs: Pair<String, Any>
    ): Mono<SysModule> {
        if (form.id == form.parent)
            return MessageException("parentNotSame", "Parent can't be same as self.").toMono()
        return super.handleUpdate(id, form, t, *pairs)
    }

    override fun SysModule.getId(): String {
        return this.id
    }

    fun findTree(): Mono<Page<SysModuleModel>> {
        val list = treeCache.get()
        return Mono.just(
            PageImpl(list)
        )
    }
}