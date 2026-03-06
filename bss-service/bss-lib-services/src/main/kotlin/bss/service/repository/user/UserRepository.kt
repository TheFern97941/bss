package bss.service.repository.user

import bss.core.page.DEFAULT_OPTIONS
import bss.core.page.PageReactiveMongoRepository
import bss.core.type.Locales
import bss.service.entity.user.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface UserRepository : CustomizedUserRepository, PageReactiveMongoRepository<User, ObjectId> {
    fun findByUid(uid: Int): Mono<User>
    fun findByEmail(email: String): Mono<User>
    fun findByUsername(username: String): Mono<User>
    fun findByPhone(phone: String): Mono<User>
    fun existsByUid(uid: Int): Mono<Boolean>
    fun existsByEmail(email: String): Mono<Boolean>
    fun existsByUsername(username: String): Mono<Boolean>
    fun existsByPhone(phone: String): Mono<Boolean>
    fun findByInviteCode(inviteCode: String): Flux<User>
    fun findAllByParentUserIdIn(parentUserId: List<ObjectId>): Flux<User>
}

interface CustomizedUserRepository {
    fun incrLoginError(id: ObjectId): Mono<User>
    fun clearLoginError(id: ObjectId): Mono<User>
    fun savePwd(id: ObjectId, encodePwd: String): Mono<User>
    fun changeLocale(userId: ObjectId, locale: Locales): Mono<User>

    fun unlock(id: ObjectId): Mono<User>

    fun setParentUserId(id: ObjectId, parentUserId: ObjectId): Mono<User>
}

class CustomizedUserRepositoryImpl(private val reactiveMongoTemplate: ReactiveMongoTemplate) :
    CustomizedUserRepository {

    override fun unlock(id: ObjectId): Mono<User> {
        val criteria = Criteria.where(User::id.name).`is`(id)
        val q = Query.query(criteria)
        val u = Update()
            .set("lock", 0)
            .currentDate(User::updatedAt.name)
            .inc(User::version.name, 1)
        return reactiveMongoTemplate.findAndModify(
            q,
            u,
            DEFAULT_OPTIONS,
            User::class.java
        )
    }

    override fun setParentUserId(id: ObjectId, parentUserId: ObjectId): Mono<User> {
        val criteria = Criteria.where(User::id.name).`is`(id)
        val q = Query.query(criteria)
        val u = Update()
            .set(User::parentUserId.name, parentUserId)
            .currentDate(User::updatedAt.name)
            .inc(User::version.name, 1)
        return reactiveMongoTemplate.findAndModify(
            q,
            u,
            DEFAULT_OPTIONS,
            User::class.java
        )
    }

    override fun incrLoginError(id: ObjectId): Mono<User> {
        val criteria = Criteria.where(User::id.name).`is`(id)
        val q = Query.query(criteria)
        val u = Update()
            .inc(User::lock.name, 1)
            .currentDate(User::updatedAt.name)
            .inc(User::version.name, 1)
        return reactiveMongoTemplate.findAndModify(
            q,
            u,
            DEFAULT_OPTIONS,
            User::class.java
        )
    }

    override fun clearLoginError(id: ObjectId): Mono<User> {
        val criteria = Criteria.where(User::id.name).`is`(id)
        val q = Query.query(criteria)
        val u = Update()
            .set(User::lock.name, 0)
            .currentDate(User::updatedAt.name)
            .inc(User::version.name, 1)
        return reactiveMongoTemplate.findAndModify(
            q,
            u,
            DEFAULT_OPTIONS,
            User::class.java
        )
    }

    override fun savePwd(id: ObjectId, encodePwd: String): Mono<User> {
        val criteria = Criteria.where(User::id.name).`is`(id)
        val q = Query.query(criteria)
        val u = Update()
            .set(User::password.name, encodePwd)
            .currentDate(User::updatedAt.name)
            .inc(User::version.name, 1)
        return reactiveMongoTemplate.findAndModify(
            q,
            u,
            DEFAULT_OPTIONS,
            User::class.java
        )
    }

    override fun changeLocale(userId: ObjectId, locale: Locales): Mono<User> {
        val criteria = Criteria.where(User::id.name).`is`(userId)
        val q = Query.query(criteria)
        val u = Update()
            .set(User::locale.name, locale)
            .currentDate(User::updatedAt.name)
            .inc(User::version.name, 1)
        return reactiveMongoTemplate.findAndModify(
            q,
            u,
            DEFAULT_OPTIONS,
            User::class.java
        )
    }
}
