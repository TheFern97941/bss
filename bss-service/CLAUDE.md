# BSS Service 项目配置

## 技术栈
- **框架**: Spring Boot 3.x + Spring WebFlux (响应式编程)
- **语言**: Kotlin
- **构建工具**: Maven
- **数据库**: JPA/Hibernate + MongoDB
- **包结构**: 
  - bss-api-admin: 管理后台API
  - bss-lib-core: 核心库
  - bss-lib-services: 服务层

## 项目约定
- 使用 Kotlin 数据类 (data class)
- JPA 实体类使用 @Entity 注解
- Repository 继承 JpaRepository
- Service 层处理业务逻辑
- Controller 使用 @RestController
- 使用 WebFlux 的 Mono/Flux 进行响应式编程
- 异步非阻塞 I/O 处理

## 代码风格
- 4空格缩进
- 使用 Kotlin 惯用语法
- 优先使用不可变数据结构