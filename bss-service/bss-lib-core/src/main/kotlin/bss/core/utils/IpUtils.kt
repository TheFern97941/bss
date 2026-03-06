package bss.core.utils

import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.server.ServerWebExchange

object IpUtils {
    fun getIp(serverWebExchange: ServerWebExchange): String? {
        val request: ServerHttpRequest = serverWebExchange.request
        val headers: HttpHeaders = request.getHeaders()
        var ip: String? = headers.getFirst("x-forwarded-for")
        if (!ip.isNullOrEmpty() && !"unknown".equals(ip, ignoreCase = true)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            }
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = headers.getFirst("Proxy-Client-IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = headers.getFirst("WL-Proxy-Client-IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = headers.getFirst("HTTP_CLIENT_IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = headers.getFirst("X-Real-IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.remoteAddress?.address?.hostAddress
        }
        return ip?.replace(":".toRegex(), ".")
    }

}