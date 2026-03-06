package bss.redis

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.Topic
import java.util.*
import java.util.concurrent.CountDownLatch

// junit 4 需要
// @RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedisChannelTests{

	@Autowired
	lateinit var streamRedisTemplate: ReactiveRedisTemplate<String, String>

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
		val cdl = CountDownLatch(count)
		for (i in 1..count) {
			streamRedisTemplate.convertAndSend(channelName, "$i - msg test")
				.subscribe{
					cdl.countDown()
				}
		}

		println("wait msg send")
		cdl.await()
		println("msg send over")
	}

	@Test
	fun listenChannelTest() {
		val cdl = CountDownLatch(count)

		streamRedisTemplate
			.listenTo(ChannelTopic(channelName))
			.doOnNext { println("msg listen -> $it") }
			.repeat()
			.subscribe{}

		println("wait listen msg")
		cdl.await()
		println("wait listen msg end")
	}

	@Test
	fun listenChannelTest1() {
		val cdl = CountDownLatch(count)
		streamRedisTemplate
			.listenTo(ChannelTopic(channelName))
			.doOnNext { println("msg listen -> $it") }
			.subscribe{
				cdl.countDown()
			}

		println("wait listen msg1")
		cdl.await()
		println("wait listen msg end1")
	}
}

