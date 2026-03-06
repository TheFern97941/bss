package bss.s3

import bss.core.s3.S3UploadService
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.util.concurrent.CountDownLatch

// junit 4 需要
// @RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class S3Tests{

	@Autowired
	lateinit var seService: S3UploadService

	@BeforeAll
	fun setup(): Unit {
		println(">> Setup")
	}

	@AfterAll
	fun shutdown(): Unit {
		println(">> Tear down")
	}

	val channelName = "janChannel"
	val count = 10;

	@Test
	fun helloTest() {
		var file = File("/Users/TheFern/Downloads/bt.jpeg")
		val cdl = CountDownLatch(1)
		seService.saveImg(file.readBytes(), "/test/bt1", true)
			.subscribe{
				cdl.countDown()
			}

		cdl.await()
		println("task end!")
	}

}

