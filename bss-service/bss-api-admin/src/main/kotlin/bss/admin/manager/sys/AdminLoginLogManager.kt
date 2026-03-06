package bss.admin.manager.sys

import bss.service.entity.sys.Admin
import bss.service.repository.sys.LoginLogRepository
import bss.service.services.sys.AbstractBaseLoginLogService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class AdminLoginLogManager(
    loginLogRepository: LoginLogRepository,
    objectMapper: ObjectMapper,
) : AbstractBaseLoginLogService<Admin>(loginLogRepository, objectMapper)
