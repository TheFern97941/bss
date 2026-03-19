# BSS 项目结构

## 模块说明

### 管理员端前端
- **bss-admin**


### 用户端前端
- **bss-h5**

### 服务器端 (bss-service)
- **bss-api-admin**: 管理员端 API 服务
- **bss-api-user**: 用户端 API 服务
- **bss-lib-core**: 核心库
- **bss-lib-services**: 服务层

## 开发约定
- 优先使用现成的第三方库和项目公共库，都不能满足需求时再自行编写代码
- 添加功能时需区分管理员端和用户端，对应修改相应模块
  - 管理员端功能: bss-admin (前端) + bss-api-admin (后端)
  - 用户端功能: bss-h5 (前端) + bss-api-user (后端)
