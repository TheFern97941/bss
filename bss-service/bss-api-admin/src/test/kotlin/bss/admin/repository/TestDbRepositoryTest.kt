package bss.admin.repository

import bss.core.result.Result
import bss.service.entity.test.TestDb
import bss.service.enums.StatusEnums
import bss.service.repository.test.TestDbRepository
import com.github.javafaker.Faker
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Flux
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.jvm.optionals.getOrDefault

// junit 4 需要
// @RunWith(SpringRunner::class)
@SpringBootTest(
	// 如果服务在启动状态, 就随机一个端口, 启动测试
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@AutoConfigureWebTestClient
class TestDbRepositoryTest{

	@Autowired
	lateinit var testDbRepository: TestDbRepository

	@BeforeAll
	fun setup(): Unit {
		println(">> Setup")
	}

	@AfterAll
	fun shutdown(): Unit {
		println(">> Tear down")
	}

	@Test
	fun randomInsert() {
		// 保持50条数据

		val dataCount = testDbRepository.count().blockOptional().getOrDefault(0)
		if (dataCount >= 50L) {
			return
		}

		val faker = Faker.instance()

		Flux.range(dataCount.toInt(), 50)
			.map {
				TestDb(
					name = faker.name().fullName(),
					age = faker.number().randomDigit(),
					amount = faker.number().randomDigit().toBigDecimal(),
					createDay = LocalDate.now().plusDays(faker.random().nextInt(-5, 5).toLong()),
					status = StatusEnums.NORMAL,
				)
			}
			.flatMap { testDbRepository.save(it) }
			.doOnNext { println(it) }
			.collectList()
			.block()


		println(" task end! ")
	}

	@Test
	fun helloTest() {
		testDbRepository.save(TestDb(
			name = "test2",
			age = 10,
			amount = BigDecimal.TEN,
		)).block()

		var tdList = testDbRepository.findAll().collectList().block()
		println(" $tdList ")

		// val entity = restTemplate.getForEntity<String>("/hello")
	}

	@Test
	fun exampleTest(@Autowired webClient: WebTestClient) {
		webClient
			.get()
			.uri("/test/hello")
			.exchange()
			.expectStatus().isOk
			.expectBody<Result<String>>()
			.value {
				"Hello kotlin".equals(it.data)
			}
			//.isEqualTo(Result.success("Hello kotlin"))
	}
}

