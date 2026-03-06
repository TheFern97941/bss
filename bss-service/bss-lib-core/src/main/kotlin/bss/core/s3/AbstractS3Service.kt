package bss.core.s3

import bss.core.exception.MessageException
import bss.core.properties.S3
import bss.core.s3.S3DownloadService.FluxResponse
import bss.core.s3.S3DownloadService.FluxResponseProvider
import mu.KotlinLogging
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.core.io.buffer.DataBufferUtils
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import software.amazon.awssdk.http.SdkHttpResponse
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import java.nio.ByteBuffer

private val log = KotlinLogging.logger {}

abstract class AbstractS3Service(
    val s3client: S3AsyncClient,
    val dataBufferFactory: DataBufferFactory,
    val s3: S3
) {

    fun downloadFile(fileKey: String): Mono<ByteArray> {
        val bucket = "/${s3.bucket}"
        return if(fileKey.startsWith(bucket)) {
            doDown(fileKey.substring(bucket.length))
        }else{
            doDown(fileKey)
        }
    }

    private fun doDown(fileKey: String): Mono<ByteArray> {
        val request: GetObjectRequest = GetObjectRequest.builder().bucket(s3.bucket).key(fileKey).build()
        return Mono.fromFuture(s3client.getObject(request, FluxResponseProvider())).flatMap { response: FluxResponse ->
            val sdkResponse = response.sdkResponse!!
            checkResult(sdkResponse.sdkHttpResponse())
            log.info(" length={}", sdkResponse.contentLength())
            mergeByteBuffers(response.flux ?: throw MessageException("error", "严重错误"))
        }
    }

    private fun mergeByteBuffers(dataByteFlux: Flux<ByteBuffer>): Mono<ByteArray> {
        return mergeDataBuffersFlux(dataByteFlux.map {
            dataBufferFactory.wrap(it)
        })
    }

    fun mergeDataBuffersFlux(dataBufferFlux: Flux<DataBuffer>): Mono<ByteArray> {
        return DataBufferUtils.join(dataBufferFlux).map { dataBuffer: DataBuffer ->
            val bytes = ByteArray(dataBuffer.readableByteCount())
            dataBuffer.read(bytes)
            DataBufferUtils.release(dataBuffer)
            bytes
        }
    }

    companion object {
        // Helper used to check return codes from an API call
        private fun checkResult(sdkResponse: SdkHttpResponse) {
            if (sdkResponse.isSuccessful) {
                return
            }
            throw MessageException("file.downloadError", "${sdkResponse.statusCode()}:${sdkResponse.statusText()}")
        }
    }
}