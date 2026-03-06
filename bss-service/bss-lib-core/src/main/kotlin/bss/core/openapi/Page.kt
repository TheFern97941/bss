package bss.core.openapi

/**
 * 页集
 */
class Page<T>(
    val pageSize: Int,
    val current: Int,
    val total: Int,
    val data: List<T>,
) {
}