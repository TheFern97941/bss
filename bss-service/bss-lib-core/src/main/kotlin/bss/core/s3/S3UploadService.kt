package bss.core.s3

import bss.core.exception.MessageException
import bss.core.properties.CoreProperties
import bss.core.utils.ImagesUtils
import mu.KotlinLogging
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestHeader
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toMono
import software.amazon.awssdk.core.SdkResponse
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.http.SdkHttpResponse
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.*
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.net.URLEncoder
import java.nio.ByteBuffer
import java.util.*
import java.util.function.Consumer
import javax.imageio.ImageIO

private val log = KotlinLogging.logger {}

/**
 * @author Philippe
 */
@Service
class S3UploadService(s3client: S3AsyncClient, dataBufferFactory: DataBufferFactory, coreProperties: CoreProperties) :
    AbstractS3Service(s3client, dataBufferFactory, coreProperties.s3) {

    fun saveImgByBase64(imgBase64: String, path: String, isScale: Boolean): Mono<Boolean> =
        saveImg(Base64.getDecoder().decode(imgBase64), path, isScale)

    fun saveImg(path: String, image: BufferedImage): Mono<Boolean> {
        //{pathType}/${id}
        return Mono.zip(
            uploadWrite(image, path, "webp"), uploadWrite(image, path, "png"), uploadWrite(image, path, "jpg")
        ).thenReturn(true)
    }

    fun saveImg(data: ByteArray, path: String, isScale: Boolean): Mono<Boolean> {
        return Mono.fromCallable {
            val image: BufferedImage = ImageIO.read(ByteArrayInputStream(data))
            if (isScale) ImagesUtils.scale(image, 173 * 2) else image
        }.publishOn(Schedulers.boundedElastic()).flatMap {
            Mono.zip(
                uploadWrite(it, path, "webp"), uploadWrite(it, path, "png"), handlerJpg(isScale, it, path, data)
            )
        }.thenReturn(true)
    }

    private fun handlerJpg(
        isScale: Boolean,
        it: BufferedImage,
        path: String,
        data: ByteArray,
    ): Mono<ResponseEntity<UploadResult>> {
        val uploadWrite = if (isScale) {
            uploadWrite(it, path, "jpeg")
        } else {
            uploadHandler(
                "$path.jpg", MediaType.IMAGE_JPEG, data
            )
        }
        return uploadWrite
    }

    private fun uploadWrite(
        output: BufferedImage,
        path: String,
        format: String,
    ): Mono<ResponseEntity<UploadResult>> {
        val data = ImagesUtils.write(format, output)
        return uploadHandler(
            "$path.$format", ImagesUtils.mediaType(format), data
        )
    }

    fun uploadHandler(
        fileKey: String,
        mediaType: MediaType,
        file: File,
        metadata: Map<String, String> = mapOf()
    ): Mono<ResponseEntity<UploadResult>> {
        // 确保所有元数据键都以 "x-amz-meta-" 开头
        val prefixedMetadata = metadata.mapKeys { (key, _) -> "x-amz-meta-$key" }

        val request = PutObjectRequest.builder()
            .bucket(s3.bucket)
            .key(fileKey)
            .contentType(mediaType.type)
            .metadata(prefixedMetadata)
            .build()
        val future = s3client.putObject(request, AsyncRequestBody.fromFile(file))

        return Mono.fromFuture(future).map { response ->
            checkResult(response)
            ResponseEntity.status(HttpStatus.CREATED).body(UploadResult(HttpStatus.CREATED, arrayOf(fileKey)))
        }
    }
    /**
     * Standard file upload.
     */
    fun uploadHandler(
        fileKey: String,
        mediaType: MediaType,
        body: ByteArray,
        metadata: Map<String, String> = mapOf(),
    ): Mono<ResponseEntity<UploadResult>> {
        // 确保所有元数据键都以 "x-amz-meta-" 开头
        val prefixedMetadata = metadata.mapKeys { (key, _) -> "x-amz-meta-$key" }

        val request = PutObjectRequest.builder()
            .bucket(s3.bucket)
            .key(fileKey)
            .contentType(mediaType.type)
            .metadata(prefixedMetadata)
            .contentLength(body.size.toLong())
            .build()

        val future = s3client.putObject(request, AsyncRequestBody.fromBytes(body))

        return Mono.fromFuture(future).map { response ->
            checkResult(response)
            ResponseEntity.status(HttpStatus.CREATED).body(UploadResult(HttpStatus.CREATED, arrayOf(fileKey)))
        }
    }


    fun deleteFile(fileKey: String): Mono<Boolean> {
        val deleteObjectRequest = DeleteObjectRequest.builder().bucket(s3.bucket).key(fileKey).build()
        val future = s3client.deleteObject(deleteObjectRequest)
        return Mono.fromFuture(future).map { response: DeleteObjectResponse ->
            checkResult(response)
            true
        }
    }

    fun isExist(
        fileKey: String,
    ): Mono<Boolean> {
        val objectRequest = HeadObjectRequest.builder().key(fileKey).bucket(s3.bucket).build()

        val future = s3client.headObject(objectRequest)

        return Mono.fromFuture(future).map { response: HeadObjectResponse ->
            checkResult(response)
            true
        }.switchIfEmpty(Mono.just(false)).onErrorResume(NoSuchKeyException::class.java) { false.toMono() }
    }

    fun multipartPublicUploadHandler(
        @RequestHeader
        headers: HttpHeaders?,
        key: String,
        parts: List<FilePart>,
    ): Flux<FileResult> {
        return Flux.fromIterable(parts)
            .flatMap { part -> saveFile(headers, key, s3.bucket, part) }
    }

    protected fun saveFile(
        headers: HttpHeaders?,
        key: String,
        bucket: String?,
        part: FilePart
    ): Mono<FileResult> {
        val metadata: MutableMap<String, String> = mutableMapOf()
        val filename = part.filename()
        metadata["filename"] = URLEncoder.encode(filename, Charsets.UTF_8)
        var mt = part.headers().contentType
        if (mt == null) {
            mt = MediaType.APPLICATION_OCTET_STREAM
        }
        val id = UUID.randomUUID().toString().replace("-", "")
        val filekey = key + id + getFileExtensionFromMediaType(mt.toString())

        return multipartUpload(filekey, bucket, part, metadata.toMap(), mt, ObjectCannedACL.PUBLIC_READ)
            .map { response: CompleteMultipartUploadResponse ->
                checkResult(response)
                FileResult(id = id, fileKey = filekey, filename = filename)
            }
    }

    fun multipartUploadHandler(
        filekey: String,
        parts: List<FilePart>,
        metadata: Map<String, String>
    ): Flux<CompleteMultipartUploadResponse> {
        return Flux.fromIterable(parts)
            .flatMap { part ->
                var mt = part.headers().contentType
                if (mt == null) {
                    mt = MediaType.APPLICATION_OCTET_STREAM
                }
                multipartUpload(filekey, s3.bucket, part, metadata, mt)
            };
    }

    fun multipartUpload(
        filekey: String,
        part: FilePart,
        acl: ObjectCannedACL? = null,
    ): Mono<CompleteMultipartUploadResponse> {
        var mediaType = part.headers().contentType
        if (mediaType == null) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM
        }
        val metadata = mapOf("name" to URLEncoder.encode(part.filename(), Charsets.UTF_8))
        return multipartUpload(filekey, s3.bucket, part, metadata, mediaType, acl)
    }

    /**
     *
     */
    fun multipartUpload(
        filekey: String,
        bucket: String?,
        part: FilePart,
        metadata: Map<String, String>,
        mediaType: MediaType,
        acl: ObjectCannedACL? = null,
    ): Mono<CompleteMultipartUploadResponse> {
        // Generate a filekey for this upload
        log.info("[I137] saveFile: filekey={}, filename={}, s3={},s3client:{}", filekey, part.filename(), s3, s3client)

        // Create multipart upload request
        val build =
            CreateMultipartUploadRequest.builder().contentType(mediaType.toString()).key(filekey).metadata(metadata)
                .acl(acl)
        if (acl != null) {
            build.acl(acl)
        }
        val uploadRequest = s3client.createMultipartUpload(
            build
                .bucket(bucket).build()
        )

        // This variable will hold the upload state that we must keep
        // around until all uploads complete
        val uploadState = UploadState(bucket, filekey)
        return Mono
            .fromFuture(uploadRequest)
            .flatMapMany { response: CreateMultipartUploadResponse ->
                checkResult(response)
                uploadState.uploadId = response.uploadId()
                log.info("[I183] uploadId={}", response.uploadId())
                part.content()
            }
            .bufferUntil { buffer: DataBuffer ->
                uploadState.buffered += buffer.readableByteCount()
                if (uploadState.buffered >= s3.multipartMinPartSize) {
                    log.info(
                        "[I173] bufferUntil: returning true, bufferedBytes={}, partCounter={}, uploadId={}",
                        uploadState.buffered,
                        uploadState.partCounter,
                        uploadState.uploadId
                    )
                    uploadState.buffered = 0
                    true
                } else {
                    false
                }
            }
            .map { buffers: List<DataBuffer> -> concatBuffers(buffers) }
            .flatMap { buffer: ByteBuffer -> uploadPart(uploadState, buffer) }.onBackpressureBuffer()
            .reduce(uploadState) { state: UploadState, completedPart: CompletedPart ->
                log.info("[I188] completed: partNumber={}, etag={}", completedPart.partNumber(), completedPart.eTag())
                state.completedParts[completedPart.partNumber()] = completedPart
                state
            }
            .flatMap { state: UploadState -> completeUpload(state) }
    }

    /**
     * Upload a single file part to the requested bucket
     * @param uploadState
     * @param buffer
     * @return
     */
    private fun uploadPart(uploadState: UploadState, buffer: ByteBuffer): Mono<CompletedPart> {
        val partNumber = ++uploadState.partCounter
        log.info("[I218] uploadPart: partNumber={}, contentLength={}", partNumber, buffer.capacity())
        val request = s3client.uploadPart(
            UploadPartRequest.builder().bucket(uploadState.bucket).key(uploadState.filekey).partNumber(partNumber)
                .uploadId(uploadState.uploadId).contentLength(buffer.capacity().toLong()).build(),
            AsyncRequestBody.fromPublisher(Mono.just(buffer))
        )
        return Mono.fromFuture(request).map { uploadPartResult: UploadPartResponse ->
            checkResult(uploadPartResult)
            log.info("[I230] uploadPart complete: part={}, etag={}", partNumber, uploadPartResult.eTag())
            CompletedPart.builder().eTag(uploadPartResult.eTag()).partNumber(partNumber).build()
        }
    }

    private fun completeUpload(state: UploadState): Mono<CompleteMultipartUploadResponse> {
        log.info(
            "[I202] completeUpload: bucket={}, filekey={}, completedParts.size={}",
            state.bucket,
            state.filekey,
            state.completedParts.size
        )
        val multipartUpload = CompletedMultipartUpload.builder().parts(state.completedParts.values).build()

        val build = CompleteMultipartUploadRequest.builder()
            .bucket(state.bucket)
            .uploadId(state.uploadId)
            .multipartUpload(multipartUpload).key(state.filekey).build()
        return Mono.fromFuture(
            s3client.completeMultipartUpload(
                build
            )
        )
    }

    /**
     * Holds upload state during a multipart upload
     */
    internal class UploadState(val bucket: String?, val filekey: String) {
        var uploadId: String? = null
        var partCounter = 0
        var completedParts: MutableMap<Int, CompletedPart> = HashMap()
        var buffered = 0
    }

    companion object {
        private fun concatBuffers(buffers: List<DataBuffer>): ByteBuffer {
            log.info("[I198] creating BytBuffer from {} chunks", buffers.size)
            var partSize = 0
            for (b in buffers) {
                partSize += b.readableByteCount()
            }
            val partData = ByteBuffer.allocate(partSize)
            buffers.forEach(Consumer { buffer: DataBuffer -> partData.put(buffer.toByteBuffer()) })

            // Reset read pointer to first byte
            partData.rewind()
            log.info("[I208] partData: size={}", partData.capacity())
            return partData
        }

        /**
         *
         */
        private fun checkResult(result: SdkResponse) {
            if (result.sdkHttpResponse() == null || !result.sdkHttpResponse().isSuccessful) {
                val httpResponse: SdkHttpResponse = result.sdkHttpResponse()
                throw MessageException("file.uploadError", "${httpResponse.statusCode()}:${httpResponse.statusText()}")
            }
        }

        val mediaTypeToFileExtension = mapOf(
            "image/jpeg" to ".jpg",
            "image/jpg" to ".jpg",
            "image/png" to ".png",
            "application/vnd.android.package-archive" to ".apk",
        )

        // 获取文件后缀的函数
        fun getFileExtensionFromMediaType(mediaType: String): String {
            val fileExtension = mediaTypeToFileExtension[mediaType]

            if (fileExtension == null) {
                if (mediaType.startsWith("image/")) {
                    return ".jpg"
                }
                if (mediaType.startsWith("video/")) {
                    return ".mp4"
                }
                if (mediaType.startsWith("application/vnd.android.package-archive")) {
                    return ".apk"
                }
                throw IllegalArgumentException("未知的 MediaType: $mediaType")
            }

            return fileExtension
        }
    }
}