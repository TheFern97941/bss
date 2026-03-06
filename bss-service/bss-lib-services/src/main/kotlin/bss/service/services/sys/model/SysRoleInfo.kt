package bss.service.services.sys.model

import bss.service.entity.sys.SysModule
import java.util.concurrent.ConcurrentMap

data class SysRoleInfo(
    /**
     * 名称
     */
    var id: String,
    var name: String,
    var moduleMap: ConcurrentMap<String, SysModule>
)