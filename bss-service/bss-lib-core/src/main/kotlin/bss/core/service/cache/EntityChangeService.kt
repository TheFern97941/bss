package bss.core.service.cache

import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import java.util.function.Consumer
import kotlin.reflect.KClass


class EntityChangeService<K : Any>(
    val reactiveStringRedisTemplate: ReactiveStringRedisTemplate,
    val toString: (k: K) -> String,
    val toKey: (str: String) -> K,
) {

    final inline fun <reified T : Any> listen(configConsumer: Consumer<K>) {
        listen(T::class, configConsumer)
    }

    fun <T : Any> listen(cls: KClass<T>, configConsumer: Consumer<K>) {
        val name = cls.simpleName
        reactiveStringRedisTemplate.listenToChannel(getKey(name!!))
            .doOnNext { message -> configConsumer.accept(toKey(message.message)) }
            .repeat()
            .subscribe()
    }

    final fun <T : Any> fireChangeEvent(cls: KClass<T>, id: K) {
        val name = cls.simpleName
        reactiveStringRedisTemplate.convertAndSend(getKey(name!!), toString(id)).subscribe()
    }

    final inline fun <reified T : Any> fireChangeEvent(id: K) {
        val name = T::class.simpleName
        reactiveStringRedisTemplate.convertAndSend(getKey(name!!), toString(id)).subscribe()
    }

    final fun getKey(name: String): String {
        return EVENT_KEY + name
    }


    companion object {
        const val EVENT_KEY = "e:change:"
    }
}