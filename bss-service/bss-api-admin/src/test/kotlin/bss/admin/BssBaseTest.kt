package bss.admin

import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient


// junit 4 需要
// @RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BssBaseTest{

	protected val userId = ObjectId("677e86082c18880077365916")
	protected val token = "PV2LrxLGtixbSxBa31aKs8K6wvZG0EAC"
	protected val tokenHeaderName = "Authorization"

	@Autowired
	lateinit var objectMapper: ObjectMapper;

	@Autowired
	lateinit var webClient: WebTestClient

	@BeforeAll
	fun setup(): Unit {
		println(">> Setup")
	}

	@AfterAll
	fun shutdown(): Unit {
		println(">> Tear down")
	}
}

