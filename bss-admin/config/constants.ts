/**
 * 环境配置常量
 * 集中管理所有的服务器地址配置
 */

// API服务器配置
export const API_CONFIG = {
  HOST: 'localhost',
  PORT: '6000',
  get BASE_URL() {
    return `http://${this.HOST}:${this.PORT}`;
  },
};

// 文件服务器配置
export const FILE_CONFIG = {
  HOST: 'localhost',
  PORT: '9000',
  get BASE_URL() {
    return `http://${this.HOST}:${this.PORT}`;
  },
};

// 环境配置
export const ENV_CONFIG = {
  DEV: 'dev',
  TEST: 'test',
  PRE: 'pre',
  PROD: 'prod',
};

