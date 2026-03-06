package bss.core.properties

import software.amazon.awssdk.regions.Region
import java.net.URI

class S3(
    var region: Region = Region.US_EAST_1,
    var endpoint: URI? = null,
    var endpointOut: URI? = null,
    var accessKeyId: String? = null,
    var secretAccessKey: String? = null,

    // Bucket name we'll be using as our backend storage
    var bucket: String? = null,

    // AWS S3 requires that file parts must have at least 5MB, except
    // for the last part. This may change for other S3-compatible services, so let't
    // define a configuration property for that
    var multipartMinPartSize: Int = 5 * 1024 * 1024,
) {
    override fun toString(): String {
        return "S3(region=$region, endpoint=$endpoint, endpointOut=$endpointOut, accessKeyId=$accessKeyId, secretAccessKey=$secretAccessKey, bucket=$bucket, multipartMinPartSize=$multipartMinPartSize)"
    }
}