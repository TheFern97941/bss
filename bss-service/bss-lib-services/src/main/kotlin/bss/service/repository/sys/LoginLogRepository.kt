package bss.service.repository.sys

import bss.core.page.PageReactiveMongoRepository
import bss.service.entity.sys.LoginLog
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
interface LoginLogRepository : PageReactiveMongoRepository<LoginLog, ObjectId>