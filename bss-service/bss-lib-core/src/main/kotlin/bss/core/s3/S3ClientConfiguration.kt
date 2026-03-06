package bss.core.s3

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.utils.StringUtils
import bss.core.properties.CoreProperties
import java.time.Duration

val logger = mu.KotlinLogging.logger {}

@Configuration
class S3ClientConfiguration(coreProperties: CoreProperties) {
    val s3 = coreProperties.s3

    @Bean
    fun s3Presigner(credentialsProvider: AwsCredentialsProvider?): S3Presigner =
        S3Presigner.builder()
            .region(s3.region)
            .endpointOverride(s3.endpoint)
            .credentialsProvider(credentialsProvider)
            .serviceConfiguration(s3Configuration())
            .build()

    @Bean
    fun s3PresignerOut(credentialsProvider: AwsCredentialsProvider?): S3Presigner =
        S3Presigner.builder()
            .region(s3.region)
            .endpointOverride(s3.endpointOut)
            .credentialsProvider(credentialsProvider)
            .serviceConfiguration(s3Configuration())
            .build()

    @Bean
    fun s3client(credentialsProvider: AwsCredentialsProvider?): S3AsyncClient {
        val httpClient = NettyNioAsyncHttpClient.builder()
            .writeTimeout(Duration.ZERO)
            .maxConcurrency(128)
            .build()
        logger.info { "s3client:[${s3.endpoint}]" }

        return S3AsyncClient.builder()
            .httpClient(httpClient)
            .region(s3.region)
            .credentialsProvider(credentialsProvider)
            .serviceConfiguration(s3Configuration())
            .endpointOverride(s3.endpoint)
            .build()
    }

    @Bean
    fun s3Configuration(): S3Configuration = S3Configuration.builder()
        .checksumValidationEnabled(false)
        .chunkedEncodingEnabled(true)
        .build()

    @Bean
    fun awsCredentialsProvider(): AwsCredentialsProvider {
        return if (StringUtils.isBlank(s3.accessKeyId)) {
            DefaultCredentialsProvider.create()
        } else {
            AwsCredentialsProvider {
                val credentials: AwsCredentials =
                    AwsBasicCredentials.create(s3.accessKeyId, s3.secretAccessKey)
                credentials
            }
        }
    }
}