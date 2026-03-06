package bss

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity

// junit 4 需要
// @RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BssApplicationTests{

	@Autowired
	lateinit var restTemplate: TestRestTemplate

	@BeforeAll
	fun setup(): Unit {
		println(">> Setup")
	}

	@AfterAll
	fun shutdown(): Unit {
		println(">> Tear down")
	}

	@Test
	fun helloTest() {
		val entity = restTemplate.getForEntity<String>("/hello")
		println(" ${entity.body} ")
	}
}

