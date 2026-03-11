package bss.admin.controller

import bss.core.annotation.NotSecurity
import bss.core.annotation.RestService
import bss.core.s3.S3UploadService
import io.swagger.v3.oas.annotations.Parameter
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.net.URI

@RestService(
    requestPath = "fileUpload",
    securityValue = "fileUpload",
)
class FileController(
    val s3UploadService: S3UploadService
) {

    @PostMapping
    @NotSecurity
    fun fileUpload(
        @Parameter(hidden = true)
        serverWebExchange: ServerWebExchange,
    ): Mono<String> {
        return serverWebExchange.multipartData.flatMap { data ->
            val fileParts = data.values.flatten().filterIsInstance<FilePart>()
            if (fileParts.isEmpty()) {
                return@flatMap Mono.error(IllegalArgumentException("未找到上传文件"))
            }

            val filePart = fileParts.first()
            val randomPath = "/avatar/${RandomStringUtils.randomAlphanumeric(4)}-${filePart.filename()}"

            s3UploadService.multipartUpload(randomPath, filePart)
        }.map { response ->
            val uri = URI.create(response.location())
            uri.path
        }
    }
}