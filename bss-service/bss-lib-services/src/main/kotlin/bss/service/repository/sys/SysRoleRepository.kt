package bss.service.repository.sys

import bss.core.page.PageReactiveMongoRepository
import bss.service.entity.sys.SysRole
import org.springframework.stereotype.Repository

@Repository
interface SysRoleRepository : PageReactiveMongoRepository<SysRole, String>