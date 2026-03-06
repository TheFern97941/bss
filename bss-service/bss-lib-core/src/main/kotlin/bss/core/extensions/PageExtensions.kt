package bss.core.extensions

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import java.util.function.Predicate

fun <T : Any, R : Any> Page<T>.flatMap(transformer: (t: T) -> Mono<R>): Mono<Page<R>> {
    return this.content.toFlux()
        .flatMapSequential { transformer(it) }
        .collectList()
        .map { PageImpl(it, this.pageable, this.totalElements) }
}

fun <T : Any, R : Any> Page<T>.flatMapList(transformer: (list: List<T>) -> Mono<List<R>>): Mono<Page<R>> {
    return this.content.toFlux()
        .collectList()
        .flatMap { transformer(it) }
        .map { PageImpl(it, this.pageable, this.totalElements) }
}


fun <T : Any> Page<T>.pageFilter(predicate: Predicate<T>): Mono<Page<T>> {
    return this.content.toFlux()
        .filter {  predicate.test(it) }
        .collectList()
        .map { PageImpl(it, this.pageable, this.totalElements) }
}

fun <T : Any, R : Any> Mono<Page<T>>.flatMapPage(transformer: (t: T) -> Mono<R>): Mono<Page<R>> {
    return this.flatMap { it.flatMap(transformer) }
}

fun <T : Any> Mono<Page<T>>.pageFilter(predicate: Predicate<T>): Mono<Page<T>> {
    return this.flatMap { it.pageFilter(predicate) }
}

fun <T : Any, R : Any> Mono<Page<T>>.flatMapList(transformer: (list: List<T>) -> Mono<List<R>>): Mono<Page<R>> {
    return this.flatMap { it.flatMapList(transformer) }
}