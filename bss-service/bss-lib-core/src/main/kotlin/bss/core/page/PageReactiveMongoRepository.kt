package bss.core.page

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Lazy
import org.springframework.core.convert.ConversionService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.UpdateDefinition
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.mongodb.repository.query.MongoEntityInformation
import org.springframework.data.mongodb.repository.support.SimpleReactiveMongoRepository
import org.springframework.data.repository.NoRepositoryBean
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.io.Serializable

val DEFAULT_OPTIONS = FindAndModifyOptions.options().returnNew(true)

val DEFAULT_UPSERT_OPTIONS = FindAndModifyOptions.options().returnNew(true).upsert(true)

@NoRepositoryBean
interface PageReactiveMongoRepository<T : Any, ID : Serializable> : ReactiveMongoRepository<T, ID> {
    fun findPage(form: PageQuery): Mono<Page<T>>
    fun count(query: Query): Mono<Long>
    fun findPage(query: Query, pageable: Pageable?): Mono<Page<T>>
    fun <O> findPage(
        aggregation: Aggregation,
        criteria: Criteria,
        pageable: Pageable,
        returnCls: Class<O>,
        inputType: Class<*>,
    ): Mono<Page<O>>

    fun findPage(pageable: Pageable?): Mono<Page<T>>
    fun <C> findPage(query: Query, pageable: Pageable, outType: Class<C>): Mono<Page<C>>
    fun findAndModify(
        query: Query,
        update: UpdateDefinition,
        options: FindAndModifyOptions,
        entityClass: Class<T>,
    ): Mono<T>

    fun <C> findPage(query: Query, pageable: Pageable, transform: (t: T) -> C): Mono<Page<C>>
}

private val log = KotlinLogging.logger {}

open class PageReactiveMongoRepositoryImpl<T : Any, ID : Serializable>(
    private val entityInformation: MongoEntityInformation<T, ID>,
    protected open val mongoOperations: ReactiveMongoOperations,
) : SimpleReactiveMongoRepository<T, ID>(entityInformation, mongoOperations), PageReactiveMongoRepository<T, ID> {

    @Autowired
    private lateinit var conversionService: ConversionService

    override fun findPage(form: PageQuery): Mono<Page<T>> {
        //加入反射类型转换
        val criteria = buildFindCriteria(form)
        val q = Query.query(criteria)
        form.toSort()
            .forEach { q.with(it) }
        return findPage(q, form.toPageable())
    }

    open fun buildFindCriteria(form: PageQuery): Criteria {
        return form.toCriteria(entityInformation.javaType.kotlin, conversionService)
    }

    override fun count(query: Query): Mono<Long> {
        return mongoOperations.count(query, entityInformation.javaType)
    }

    override fun findAndModify(
        query: Query,
        update: UpdateDefinition,
        options: FindAndModifyOptions,
        entityClass: Class<T>,
    ): Mono<T> {
        return mongoOperations.findAndModify(query, update, options, entityClass)
    }

    override fun findPage(query: Query, pageable: Pageable?): Mono<Page<T>> {
        return Mono.zip(
            mongoOperations.find(Query.of(query).with(pageable!!), entityInformation.javaType).collectList(),
            count(query)
        ).map { (list, count) ->
            log.debug { "findPageByQuery:[query:$query,pageable:{$pageable}]" }
            PageImpl(list, pageable, count)
        }
    }

    override fun <C> findPage(query: Query, pageable: Pageable, outType: Class<C>): Mono<Page<C>> {
        return Mono.zip<List<C>, Long>(
            mongoOperations.find(
                Query.of(query).with(pageable), outType, mongoOperations.getCollectionName(
                    entityInformation.javaType
                )
            ).collectList(), mongoOperations.count(query, entityInformation.javaType)
        ).map { (list, count) ->
            log.debug { "findPageByQuery:[query:$query,pageable:{$pageable}]" }
            PageImpl(list, pageable, count)
        }
    }

    override fun <C> findPage(query: Query, pageable: Pageable, transform: (t: T) -> C): Mono<Page<C>> {
        val collectionName = mongoOperations.getCollectionName(
            entityInformation.javaType
        )

        val collectList = mongoOperations.find(
            Query.of(query).with(pageable), entityInformation.javaType, collectionName
        ).map { transform(it) }.collectList()

        val count1 = mongoOperations.count(query, entityInformation.javaType)

        return Mono.zip<List<C>, Long>(collectList, count1).map { (list, count) ->
            log.debug { "findPageByQuery:[query:$query,pageable:{$pageable}]" }
            PageImpl(list, pageable, count)
        }
    }

    override fun <O> findPage(
        aggregation: Aggregation,
        criteria: Criteria,
        pageable: Pageable,
        returnCls: Class<O>,
        inputType: Class<*>,
    ): Mono<Page<O>> {
        return Mono.zip<List<O>, Long>(
            mongoOperations.aggregate(aggregation, inputType, returnCls).collectList(),
            mongoOperations.count(Query(criteria), inputType)
        ).map { (list, count) ->
            log.debug { "findPageByQuery:[aggregation:$aggregation,pageable:{$pageable}]" }
            PageImpl(list, pageable, count)
        }
    }

    override fun findPage(pageable: Pageable?): Mono<Page<T>> {
        return findPage(Query(), pageable)
    }


}