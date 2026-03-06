# 配置说明

## 常量配置文件

项目现在使用 `config/constants.ts` 文件来集中管理所有的服务器地址配置。

### 主要配置项

- **API_CONFIG**: API服务器配置
  - `HOST`: API服务器IP地址
  - `PORT`: API服务器端口
  - `BASE_URL`: 完整的API服务器URL

- **FILE_CONFIG**: 文件服务器配置
  - `HOST`: 文件服务器IP地址
  - `PORT`: 文件服务器端口
  - `BASE_URL`: 完整的文件服务器URL

### 使用方法

1. 修改 `config/constants.ts` 文件中的IP地址和端口
2. 所有使用这些配置的文件会自动使用新的值

### 如果需要使用环境变量

如果你希望使用环境变量而不是硬编码的值，可以：

1. 创建 `.env.local` 文件（注意：此文件不会被提交到git）
2. 在 `constants.ts` 中使用 `process.env.REACT_APP_*` 来读取环境变量

例如：
```typescript
export const API_CONFIG = {
  HOST: process.env.REACT_APP_API_HOST || '192.168.31.51',
  PORT: process.env.REACT_APP_API_PORT || '6000',
  get BASE_URL() {
    return `http://${this.HOST}:${this.PORT}`;
  },
};
```

### 已更新的文件

- `config/proxy.ts` - 代理配置现在使用常量
- `config/config.ts` - openAPI配置现在使用常量

