package bss.admin.manager.test

import bss.admin.controller.test.form.TestDbForm
import bss.core.service.AbstractCacheBroadcastService
import bss.core.service.AbstractManagerService
import bss.service.entity.test.TestDb
import bss.service.repository.test.TestDbRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class TestDbManager(val testDbRepository: TestDbRepository) :
    AbstractCacheBroadcastService<TestDb, ObjectId, TestDbForm, TestDbForm, TestDb>(testDbRepository, TestDb::class) {

    override fun getId(t: TestDb): ObjectId {
        return t.id
    }
}