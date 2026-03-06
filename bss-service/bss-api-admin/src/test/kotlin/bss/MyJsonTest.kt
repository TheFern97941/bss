package bss

import bss.service.entity.test.TestDb
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.json.JacksonTester
import java.math.BigDecimal


@JsonTest
class MyJsonTest(@Autowired val json: JacksonTester<TestDb>) {

    @Test
    fun serialize() {
        val details = TestDb(
            name = "test2",
            age = 10,
            amount = BigDecimal.TEN,
        )
        println("${json.write(details)}")
        // Assert against a `.json` file in the same package as the test
//        assertThat(json.write(details)).isEqualToJson("expected.json")
//        // Or use JSON path based assertions
//        assertThat(json.write(details)).hasJsonPathStringValue("@.make")
//        assertThat(json.write(details)).extractingJsonPathStringValue("@.make").isEqualTo("Honda")
    }

    @Test
    fun deserialize() {
        val content = "{\"make\":\"Ford\",\"model\":\"Focus\"}"
//        assertThat(json.parse(content)).isEqualTo(VehicleDetails("Ford", "Focus"))
//        assertThat(json.parseObject(content).make).isEqualTo("Ford")
    }
}