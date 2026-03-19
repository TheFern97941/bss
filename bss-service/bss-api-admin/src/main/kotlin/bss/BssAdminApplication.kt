package bss

import bss.admin.AdminSecurityFilter
import bss.admin.manager.sys.AdminAccountManager
import bss.core.page.PageManagerRepositoryFactoryBean
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.reactive.ServerWebExchangeContextFilter
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping
import reactor.tools.agent.ReactorDebugAgent

@SpringBootApplication
@EnableScheduling
@EnableReactiveMongoAuditing
@EnableReactiveMongoRepositories(
    basePackages = ["bss.core.page", "bss.service.repository.**"],
    repositoryFactoryBeanClass = PageManagerRepositoryFactoryBean::class
)
@EnableConfigurationProperties
class BssAdminApplication {

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    fun accountWebFilter(
        adminAccountManager: AdminAccountManager,
        requestMappingHandlerMapping: RequestMappingHandlerMapping,
    ) = AdminSecurityFilter(adminAccountManager, requestMappingHandlerMapping)


    @Bean
    fun serverWebExchangeContextFilter() = ServerWebExchangeContextFilter()

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun corsWebFilter(): CorsWebFilter {
        val corsConfig = CorsConfiguration()
        corsConfig.addAllowedOrigin(CorsConfiguration.ALL)
        corsConfig.maxAge = 8000L
        corsConfig.addAllowedMethod(CorsConfiguration.ALL)
        corsConfig.addAllowedHeader("Authorization")
        corsConfig.addAllowedHeader("content-type")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)

        return CorsWebFilter(source)
    }
}

fun main(args: Array<String>) {
    runApplication<BssAdminApplication>(*args) {
        ReactorDebugAgent.init()
        setBannerMode(Banner.Mode.OFF)
    }
}
