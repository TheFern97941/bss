import createMiddleware from "next-intl/middleware";
import { routing } from '@/i18n/routing';
import { NextRequest, NextResponse } from "next/server";


const intlMiddleware = createMiddleware(routing);

export const config = {
  matcher: '/((?!api|trpc|_next|_vercel|.*\\.(?:svg|png|jpg|jpeg|webp|ico|js|css|json|map|txt|woff2?)|.*opengraph-image).*)'
}

export default function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl

  // ✅ 1. 如果访问根路径 `/`，重定向到默认语言（例如 `/en`）
  if (pathname === '/') {
    const locale = routing.localeDetection !== false ? routing.defaultLocale : 'en'
    return NextResponse.redirect(new URL(`/${locale}`, request.url))
  }

  // ✅ 2. 否则交给 next-intl 的 middleware 处理
  return intlMiddleware(request)
}

