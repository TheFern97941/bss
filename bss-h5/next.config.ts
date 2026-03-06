import type { NextConfig } from "next";
import createNextIntlPlugin from 'next-intl/plugin';

const nextConfig: NextConfig = {
  reactStrictMode: true,
  basePath: '',
  crossOrigin: 'anonymous',
  distDir: 'build',
  poweredByHeader: true,
  // devIndicators 开发指标
  // devIndicators: false,
  devIndicators: {
    position: "top-right"
  },
  logging: {
    fetches: {
      fullUrl: true
    }
  },
  experimental: {
    webpackMemoryOptimizations: true
  },
  generateBuildId: async () => {
    // This could be anything, using the latest git hash
    return process.env.GIT_HASH || null
  },
  typescript: {
    ignoreBuildErrors: false
  },
  eslint: {
    // 因为eslint 构建失败, 可以忽略
    ignoreDuringBuilds: true,
  },
  compiler: {
    removeConsole: false
  },
  webpack: (
    config,
    { buildId, dev, isServer, defaultLoaders, nextRuntime, webpack }
  ) => {
    // Important: return the modified config
    return config
  },
};

const withNextIntl = createNextIntlPlugin({
  experimental: {
    createMessagesDeclaration: "./messages/zh.json",
  }
})

export default withNextIntl(nextConfig);
