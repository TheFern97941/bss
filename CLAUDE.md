# BSS 项目结构

## 模块说明

### 管理员端前端 (bss-admin)
- 框架: Umi Max 4.x + Ant Design Pro + React 18 + TypeScript
- 包管理: pnpm
- API 层: OpenAPI 自动生成 (@umijs/openapi)
- 状态管理: Umi models (hooks)
- 页面模式: MyProTable + Command 模式 (index/columns/commands/XxxForm/service 五件套)
- 核心组件: MyProTable, MyDrawerForm, Field (Amount/ObjectId/TimeZone)

### 用户端前端 (bss-h5)
- 框架: Next.js 15.4 (App Router + Turbopack) + React 19 + TypeScript
- UI: Tailwind CSS 4 + shadcn/ui (Radix UI)
- 国际化: next-intl 4.x (zh/en)
- 主题: next-themes (暗色/亮色)
- 路由: app/[locale]/ 下按功能划分

### 服务器端 (bss-service)
- 框架: Spring Boot 3.5 + WebFlux (响应式) + Kotlin
- 构建: Maven 多模块
- 数据库: MongoDB (Reactive) + Redis (Reactive, token 存储)
- 文件存储: AWS S3
- 认证: Token + Redis, 支持 Google Authenticator 2FA
- 权限: @SecurityAnnotation + 角色模块映射
- 定时任务: Quartz

#### Maven 模块依赖
```
bss-api-admin → bss-lib-services → bss-lib-core
```

#### bss-lib-core (核心库)
- auth: Token 认证框架 (AccountWebFilter, AccountContext)
- controller: 泛型 CRUD 基类 (AbstractManagerController, AbstractManagerReadOnlyController)
- service: 泛型服务基类 (AbstractManagerService → AbstractDeleteService → AbstractReadOnlyService)
- page: MongoDB 自定义分页 (PageReactiveMongoRepository, PageQuery)
- result: 统一响应包装 (Result<T>)
- s3: 文件上传下载
- cache: 内存缓存 + 广播更新
- annotation: @Auth, @NotAuth, @NotSecurity, @SecurityAnnotation, @RestService

#### bss-lib-services (服务层)
- entity: Admin, User, LoginLog, SysModule, SysRole, Setting
- repository: 各实体 Repository (继承 PageReactiveMongoRepository)
- services: SecurityService, SysRoleService, SysModuleService, SettingService

#### bss-api-admin (管理员 API)
- controller: AdminAccount, SysAdmin, SysRole, SysModule, SysSetting, File
- manager: AdminAccountManager, AdminManager, AdminLoginLogManager
- security: AdminSecurityFilter (角色 + 注解校验)

#### bss-api-user (用户端 API, 待开发)

## 开发约定
- 优先使用现成的第三方库和项目公共库，都不能满足需求时再自行编写代码
- 添加功能时需区分管理员端和用户端，对应修改相应模块
  - 管理员端功能: bss-admin (前端) + bss-api-admin (后端)
  - 用户端功能: bss-h5 (前端) + bss-api-user (后端)
