package bss.service.repository.sys

import bss.service.entity.sys.SysModule
import bss.core.page.PageReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SysModuleRepository : PageReactiveMongoRepository<SysModule, String>