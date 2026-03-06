package bss.core.s3

import bss.core.properties.CoreProperties
import bss.core.utils.DataClassUtils
import bss.core.utils.ImagesUtils
import mu.KotlinLogging
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import software.amazon.awssdk.core.async.AsyncResponseTransformer
import software.amazon.awssdk.core.async.SdkPublisher
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest
import java.nio.ByteBuffer
import java.time.Duration
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KProperty0

private val log = KotlinLogging.logger {}

/**
 * @author Philippe
 */
@Service
class S3DownloadService(
    dataBufferFactory: DataBufferFactory,
    coreProperties: CoreProperties,
    s3client: S3AsyncClient,
    val s3Presigner: S3Presigner,
    val s3PresignerOut: S3Presigner,
) : AbstractS3Service(s3client, dataBufferFactory, coreProperties.s3) {


    final inline fun <reified MODEL : Any, T : Any> transform(t: T, imgProperty: KProperty0<String?>): MODEL {
        return transform(t, imgProperty.get(), imgProperty.name)
    }

    final inline fun <reified MODEL : Any, T : Any> transform(
        t: T,
        path: String?,
        imgPropertyName: String,
        vararg pairs: Pair<String, Any>,
    ): MODEL {
        val modelCls = MODEL::class
        return if (path?.isNotBlank() == true) {
            val src = getObjectUrl("${path}.webp", "webp")
            DataClassUtils.new(modelCls, t, imgPropertyName to src, *pairs)
        } else {
            DataClassUtils.new(modelCls, t, *pairs)
        }
    }

    fun getUrl(fileKey: String): String {
        return "/${s3.bucket}/$fileKey"
    }

    fun getObjectUrl(fileKey: String, format: String? = null): String {
        val getObjectRequestBuild = GetObjectRequest.builder().bucket(s3.bucket)
        if (format != null) {
            getObjectRequestBuild.responseContentType(ImagesUtils.mediaType(format).toString())
        }

        val getObjectRequest = getObjectRequestBuild.key(fileKey).build()

        val getObjectPresignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(60))
            .getObjectRequest(getObjectRequest).build()


//        S3Presigner presigner =
        val presignedGetObjectRequest: PresignedGetObjectRequest =
            s3PresignerOut.presignGetObject(getObjectPresignRequest)
        return presignedGetObjectRequest.url().toString()
    }


    /**
     * Lookup a metadata key in a case-insensitive way.
     * @param sdkResponse
     * @param key
     * @param defaultValue
     * @return
     */
    private fun getMetadataItem(sdkResponse: GetObjectResponse, key: String, defaultValue: String): String {
        for ((key1, value) in sdkResponse.metadata().entries) {
            if (key1.equals(key, ignoreCase = true)) {
                return value
            }
        }
        return defaultValue
    }

    internal class FluxResponseProvider : AsyncResponseTransformer<GetObjectResponse, FluxResponse> {
        private var response: FluxResponse? = null

        override fun prepare(): CompletableFuture<FluxResponse> {
            response = FluxResponse()
            return response!!.cf
        }

        override fun onResponse(sdkResponse: GetObjectResponse?) {
            response!!.sdkResponse = sdkResponse
        }

        override fun onStream(publisher: SdkPublisher<ByteBuffer>) {
            response!!.flux = Flux.from(publisher)
            response!!.cf.complete(response)
        }

        override fun exceptionOccurred(error: Throwable) {
            response!!.cf.completeExceptionally(error)
        }
    }

    /**
     * Holds the API response and stream
     * @author Philippe
     */
    internal class FluxResponse {
        val cf: CompletableFuture<FluxResponse> = CompletableFuture<FluxResponse>()
        var sdkResponse: GetObjectResponse? = null
        var flux: Flux<ByteBuffer>? = null
    }


}