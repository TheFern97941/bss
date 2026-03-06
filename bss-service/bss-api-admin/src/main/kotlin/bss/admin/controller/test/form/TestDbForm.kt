package bss.admin.controller.test.form

import bss.service.enums.StatusEnums
import java.math.BigDecimal

data class TestDbForm(
    val name: String,
    val age: Int,
    val amount: BigDecimal,
    val status: StatusEnums = StatusEnums.NORMAL,
    val imgUrl: String?,
    val markdownContent: String?,
    val fullContent: String?,
)
