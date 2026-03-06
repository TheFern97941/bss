package bss.service.repository.sys.form

data class UpdateAdminForm(
    var name: String? = null,
    var title: String? = null,
    var email: String? = null,
    var avatar: String? = null,
)