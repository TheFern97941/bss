package bss.core.s3

import org.springframework.http.HttpStatus

class UploadResult(var status: HttpStatus, var keys: Array<String> = arrayOf())