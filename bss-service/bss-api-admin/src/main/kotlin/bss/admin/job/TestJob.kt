package bss.admin.job

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Component
class TestJob {

    // @Scheduled(fixedDelay = 60 * 1000)
    fun refresh() {
        println("Refreshing test job: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}")
    }
}