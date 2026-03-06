package bss.core.service

import bss.core.exception.MessageException
import bss.core.extensions.flatMapPage
import bss.core.page.PageQuery
import bss.core.page.PageReactiveMongoRepository
import bss.core.service.cache.EntityChangeService
import bss.core.utils.DataClassUtils
import jakarta.annotation.Resource
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import java.io.Serializable
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer
import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes


private val log = KotlinLogging.logger {}

abstract class AbstractCacheReadOnlyService<T : Any, K : Serializable, MODEL : Any>(
    val repository: PageReactiveMongoRepository<T, K>
) : ReadOnlyService<T, K, MODEL> {
    protected val cls: KClass<T>
    protected val modelCls: KClass<MODEL>

    init {
        val type = this::class.allSupertypes.first {
            it.classifier == AbstractCacheReadOnlyService::class
        }
        val classifier = type.arguments[0].type?.classifier ?: throw MessageException(
            "genericParameterError", "Generic parameter error"
        )
        @Suppress("UNCHECKED_CAST")
        cls = classifier as KClass<T>

        val modelClassifier = type.arguments[2].type?.classifier ?: throw MessageException(
            "genericParameterError", "Generic parameter error"
        )
        @Suppress("UNCHECKED_CAST")
        modelCls = modelClassifier as KClass<MODEL>
    }

    private var idCache: ConcurrentMap<K, T> = ConcurrentHashMap()

    @Autowired
    lateinit var entityChangeService: EntityChangeService<K>

    private val listeners: CopyOnWriteArraySet<Consumer<T>> = CopyOnWriteArraySet<Consumer<T>>()
    private val delListeners = CopyOnWriteArrayList<Consumer<K>>()

    private val isInit: AtomicBoolean = AtomicBoolean(false)
    private val onInitListeners = ArrayBlockingQueue<Runnable>(1000)

    open fun repositoryFindAll(): Flux<T> {
        return repository.findAll()
    }

    open  fun repositoryFindById(id: K): Mono<T> {
        return repository.findById(id)
    }


    protected open fun onDelCache(t: T) {

    }

    protected open fun onInit(list: List<T>) {

    }

    protected abstract fun onCache(t: T, replace: Boolean)

    public fun listen(configConsumer: Consumer<T>) {
        listeners.add(configConsumer)
    }

    fun listenOnInit(configConsumer: Runnable) {
        if (isInit.get()) {
            configConsumer.run()
            return
        }
        synchronized(isInit) {
            if (isInit.get()) {
                /*
                 * 如果已经初始化完成，直接执行
                 */
                configConsumer.run()
            } else {
                // 但是这样有可能还是进
                onInitListeners.add(configConsumer)
            }
        }
    }

    fun listenDelete(configConsumer: Consumer<K>?) {
        delListeners.add(configConsumer)
    }


    open fun init() {
        log.info("initStart:{}", cls.simpleName)

        repositoryFindAll()
            .doOnNext { cache(it, false) }
            .collectList()
            .subscribe { r ->
                log.info("initEnd:{}", cls.simpleName)
                entityChangeService.listen<T>(cls) { id ->
                    repositoryFindById(id)
                        .doOnNext { cache(it, true) }
                        .doOnNext { t ->
                            listeners.forEach { it.accept(t) }
                        }
                        .switchIfEmpty {
                            delCache(id)
                            Mono.empty()
                        }
                        .subscribe()
                }

                synchronized(isInit) {
                    isInit.set(true)
                }

                val list = ArrayList<Runnable>()
                onInitListeners.drainTo(list)
                list.forEach(Consumer { c: Runnable ->
                    try {
                        c.run()
                    } catch (e: Exception) {
                        log.error("onInitListeners error:{}", e.message, e)
                    }
                })
                this.onInit(r)
            }
    }


    fun delCache(id: K) {
        val t = idCache[id]
        if (t != null) {
            onDelCache(t)
            idCache.remove(id)
        }
        for (listener in delListeners) {
            listener.accept(id)
        }
    }

    fun cache(t: T, replace: Boolean) {
        onCache(t, replace)
        idCache[t.getId()] = t
    }

    protected abstract fun T.getId(): K

    fun findById(id: K): T? = idCache.get(id)

    fun findByIdNotNull(id: K) = findById(id) ?: throw MessageException("notFound", "Not found:${cls.simpleName} - $id")

    fun fireChangeEvent(t: T) {
        entityChangeService.fireChangeEvent(cls, t.getId())
    }

    fun fireChangeEventById(id: K) {
        entityChangeService.fireChangeEvent(cls, id)
    }

    open fun getAll(): Collection<T> {
        return idCache.values
    }


    fun findAll(): List<T> = idCache.values.toList()


    override fun findPage(form: PageQuery): Mono<Page<MODEL>> {
        return repository.findPage(form)
            .flatMapPage { toModel(it) }
    }

    open fun toModel(t: T): Mono<MODEL> {
        if (cls === modelCls) {
            @Suppress("UNCHECKED_CAST")
            return (t as MODEL).toMono()
        }
        return DataClassUtils.new(modelCls, t).toMono()
    }

    override fun findAllById(ids: Array<K>): Flux<MODEL> {
        return Flux.fromArray(ids)
            .flatMapSequential { Mono.justOrEmpty(idCache[it]) }
            .flatMapSequential { toModel(it) }
    }

    override fun findByIdToModel(id: K): Mono<MODEL> {
        val t = findById(id) ?: throw MessageException("notFound", "Not found:${cls.simpleName} - $id")
        return Mono.just(t)
            .flatMap { toModel(it) }
    }
}