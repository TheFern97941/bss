package bss.admin.controller.test

import bss.admin.controller.test.form.TestDbForm
import bss.admin.manager.test.TestDbManager
import bss.core.annotation.RestService
import bss.core.controller.AbstractManagerController
import bss.service.entity.test.TestDb
import org.bson.types.ObjectId

@RestService(requestPath = "testDb", securityValue = "testDb")
class TestDbManagerController(
    testDbManager: TestDbManager
): AbstractManagerController<TestDb, ObjectId, TestDbForm, TestDbForm, TestDb>(testDbManager)