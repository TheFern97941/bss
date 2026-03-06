package bss.core.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "core")
class CoreProperties(
    var auth: Auth = Auth(),
    var s3: S3 = S3(),
)