package bss.service.repository.test

import bss.service.entity.test.TestDb
import bss.core.page.PageReactiveMongoRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository

@Repository
interface TestDbRepository: PageReactiveMongoRepository<TestDb, ObjectId>, CustomerTestDbRepository {

}

interface CustomerTestDbRepository {

}

class CustomerTestDbRepositoryImpl(
    val mongoTemplate: ReactiveMongoTemplate
): CustomerTestDbRepository {

}