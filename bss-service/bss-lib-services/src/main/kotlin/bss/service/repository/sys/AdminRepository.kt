package bss.service.repository.sys

import bss.core.page.DEFAULT_OPTIONS
import bss.core.page.PageReactiveMongoRepository
import bss.service.entity.sys.Admin
import bss.service.repository.sys.form.UpdateAdminForm
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface AdminRepository : PageReactiveMongoRepository<Admin, ObjectId>, CustomizedAdminRepository {
    fun findByUsername(username: String): Mono<Admin>
}

interface CustomizedAdminRepository {
    fun incrLoginError(id: ObjectId): Mono<Admin>
    fun unlock(id: ObjectId): Mono<Admin>
    fun savePwd(id: ObjectId, encodePwd: String): Mono<Admin>
    fun saveAuthKey(id: ObjectId, authKey: String): Mono<Admin>
    fun unbindAuthKey(id: ObjectId): Mono<Admin>
    fun updateInfo(id: ObjectId, body: UpdateAdminForm): Mono<Admin>
}

class CustomizedAdminRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : CustomizedAdminRepository {

    override fun unlock(id: ObjectId): Mono<Admin> {
        val criteria = Criteria.where(Admin::id.name).`is`(id)
        val q = Query.query(criteria)
        val u = Update()
            .set(Admin::lock.name, 0)
            .currentDate(Admin::updatedAt.name)
            .inc(Admin::version.name, 1)
        return reactiveMongoTemplate.findAndModify(
            q,
            u,
            DEFAULT_OPTIONS,
            Admin::class.java
        )
    }

    override fun savePwd(id: ObjectId, encodePwd: String): Mono<Admin> {
        val criteria = Criteria.where(Admin::id.name).`is`(id)
        val q = Query.query(criteria)
        val u = Update()
            .set(Admin::password.name, encodePwd)
            .currentDate(Admin::updatedAt.name)
            .inc(Admin::version.name, 1)
        return reactiveMongoTemplate.findAndModify(
            q,
            u,
            DEFAULT_OPTIONS,
            Admin::class.java
        )
    }

    override fun saveAuthKey(id: ObjectId, authKey: String): Mono<Admin> {
        val criteria = Criteria.where(Admin::id.name).`is`(id)
        val q = Query.query(criteria)
        val u = Update()
            .set(Admin::authKey.name, authKey)
            .currentDate(Admin::updatedAt.name)
            .inc(Admin::version.name, 1)
        return reactiveMongoTemplate.findAndModify(
            q,
            u,
            DEFAULT_OPTIONS,
            Admin::class.java
        )
    }

    override fun unbindAuthKey(id: ObjectId): Mono<Admin> {
        val criteria = Criteria.where(Admin::id.name).`is`(id)
        val q = Query.query(criteria)
        val u = Update()
            .set(Admin::authKey.name, null)
            .currentDate(Admin::updatedAt.name)
            .inc(Admin::version.name, 1)
        return reactiveMongoTemplate.findAndModify(
            q,
            u,
            DEFAULT_OPTIONS,
            Admin::class.java
        )
    }

    override fun updateInfo(id: ObjectId, body: UpdateAdminForm): Mono<Admin> {
        val criteria = Criteria.where(Admin::id.name).`is`(id)
        val q = Query.query(criteria)
        val u = Update()
            .set(Admin::name.name, body.name)
            .set(Admin::title.name, body.title)
            .set(Admin::email.name, body.email)
            .set(Admin::avatar.name, body.avatar)
            .currentDate(Admin::updatedAt.name)
            .inc(Admin::version.name, 1)
        return reactiveMongoTemplate.findAndModify(
            q,
            u,
            DEFAULT_OPTIONS,
            Admin::class.java
        )
    }

    override fun incrLoginError(id: ObjectId): Mono<Admin> {
        val criteria = Criteria.where(Admin::id.name).`is`(id)
        val q = Query.query(criteria)
        val u = Update()
            .inc(Admin::lock.name, 1)
            .currentDate(Admin::updatedAt.name)
            .inc(Admin::version.name, 1)
        return reactiveMongoTemplate.findAndModify(
            q,
            u,
            DEFAULT_OPTIONS,
            Admin::class.java
        )
    }

}