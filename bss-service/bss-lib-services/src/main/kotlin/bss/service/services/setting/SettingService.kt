package bss.service.services.setting

import bss.core.service.AbstractCacheManagerService
import bss.service.entity.setting.AllSetting
import bss.service.entity.setting.AuthSetting
import bss.service.entity.setting.Setting
import bss.service.repository.setting.AllSettingRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

@Service
class SettingService(
    val allSettingRepository: AllSettingRepository
) : AbstractCacheManagerService<AllSetting, String, AllSetting, String, AllSetting>(allSettingRepository) {

    override fun AllSetting.getId() = this.id

    @PostConstruct
    override fun init() {
        super.init()
        initSetting(AuthSetting())
    }

     fun <T : Setting> initSetting(setting: T) {
         val id = setting::class.simpleName!!
         val allSetting = findById(id)
        if (allSetting == null) {
            allSettingRepository.save(setting)
                .doOnNext {
                    entityChangeService.fireChangeEvent<AllSetting>(id)
                }
                .subscribe()
        }
    }

    fun <T : Setting> save(setting: T): Mono<AllSetting> {
        return allSettingRepository.save(setting)
            .doOnNext {
                entityChangeService.fireChangeEvent(AllSetting::class, it.id)
            }
    }

    fun authSetting(): AuthSetting {
        return get(AuthSetting::class){ AuthSetting() }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Setting> get(cls: KClass<T>, default: () -> T): T {
        val allSetting = getAllSetting(cls) ?: return default()
        return allSetting.setting as T
    }

    fun <T : Setting> getAllSetting(cls: KClass<T>): AllSetting? {
        val id = cls.simpleName!!
        return super.findById(id)
    }

    override fun onCache(t: AllSetting, replace: Boolean) {

    }

}
