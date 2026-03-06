package bss.service.repository.setting

import bss.core.page.DEFAULT_UPSERT_OPTIONS
import bss.core.page.PageReactiveMongoRepository
import bss.service.entity.setting.AllSetting
import bss.service.entity.setting.Setting
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Repository
interface AllSettingRepository : PageReactiveMongoRepository<AllSetting, String>, CustomAllSettingRepository {
}

interface CustomAllSettingRepository {
    fun <T : Setting> save(setting: T): Mono<AllSetting>
}

class CustomAllSettingRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : CustomAllSettingRepository {

    override fun <T : Setting> save(setting: T): Mono<AllSetting> {
        val criteria = Criteria.where("id").`is`(setting::class.simpleName)
        val q = Query.query(criteria)

        val u = Update()
            .set(AllSetting::setting.name, setting)
            .set(AllSetting::settingType.name, setting::class.qualifiedName)
            .currentDate("updatedAt")
            .setOnInsert("createdAt", LocalDateTime.now())
            .inc("version", 1)

        return reactiveMongoTemplate.findAndModify(
            q, u, DEFAULT_UPSERT_OPTIONS, AllSetting::class.java
        )
    }
}