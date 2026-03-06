package bss.core.utils

import bss.core.exception.require
import com.luciad.imageio.webp.WebPWriteParam
import com.twelvemonkeys.image.ResampleOp
import org.springframework.http.MediaType
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImageOp
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.plugins.jpeg.JPEGImageWriteParam
import javax.imageio.stream.MemoryCacheImageOutputStream


val IMAGE_WEBP = MediaType("image", "webp")

object ImagesUtils {
    private val webpParam: WebPWriteParam = WebPWriteParam(Locale.getDefault())
    private val jpgParam: JPEGImageWriteParam = JPEGImageWriteParam(Locale.getDefault())

    init {
        webpParam.compressionMode = ImageWriteParam.MODE_EXPLICIT
        webpParam.compressionType = webpParam.compressionTypes[WebPWriteParam.LOSSY_COMPRESSION]
    }

    fun imageToBase64String(image: ByteArray, format: String): String {
        val base64 = Base64.getEncoder().encodeToString(image)
        return "data:image/$format;base64,$base64"
    }

    fun mediaType(format: String): MediaType {
        return when (format) {
            "webp" -> IMAGE_WEBP
            "jpg" -> MediaType.IMAGE_JPEG
            "jpeg" -> MediaType.IMAGE_JPEG
            "png" -> MediaType.IMAGE_PNG
            else -> MediaType.IMAGE_JPEG
        }
    }

    fun scale(image: BufferedImage, maxSize: Int): BufferedImage {
        val width = maxSize
        val height = maxSize * (image.height.toFloat() / image.width.toFloat())

        val resampler: BufferedImageOp = ResampleOp(
            width, height.toInt(), ResampleOp.FILTER_LANCZOS
        ) // A good default filter, see class documentation for more info
        return resampler.filter(image, null)
    }

    fun write(format: String, output: BufferedImage): ByteArray {
        val writers = ImageIO.getImageWritersByFormatName(format)
        require(writers.hasNext(), "imageio.notWriter") { "No writer for: $format" }

        val webOut = ByteArrayOutputStream()

        when (format) {
            "webp" -> {
                MemoryCacheImageOutputStream(webOut).use {
                    val writer = writers.next()
                    writer.output = it
                    writer.write(null, IIOImage(output, null, null), webpParam)
                }
            }

            "jpeg" -> {
                MemoryCacheImageOutputStream(webOut).use {
                    val writer = writers.next()
                    writer.output = it
                    writer.write(null, IIOImage(removeAlphaChannel(output), null, null), jpgParam)
                }
            }

            else -> {
                ImageIO.write(output, format, webOut)
            }
        }
        val data = webOut.toByteArray()
        return data
    }

    private fun removeAlphaChannel(img: BufferedImage): BufferedImage {
        if (!img.colorModel.hasAlpha()) {
            return img
        }
        val target: BufferedImage = createImage(img.width, img.height)
        val g = target.createGraphics()
        try {
            g.color = Color(0xffffff, false)
            g.fillRect(0, 0, img.width, img.height)
            g.drawImage(img, 0, 0, null)
        } finally {
            g.dispose()
        }
        return target
    }

    private fun createImage(width: Int, height: Int): BufferedImage {
        return BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    }
}