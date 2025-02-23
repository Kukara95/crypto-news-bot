package com.alexchern.cryptonewsbot.bot

import com.alexchern.cryptonewsbot.domain.NewsService
import com.alexchern.cryptonewsbot.gpt.GptClient
import com.alexchern.cryptonewsbot.web.client.TgBotClient
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "tg.bot")
data class TgBotProps(
    val apiKey: String,
    val groupId: Long
)

@Component
class TgBot(
    private val props: TgBotProps,
    private val newsService: NewsService,
    private val tgBotClient: TgBotClient,
    private val gptClient: GptClient
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(fixedDelay = 300_000)
    fun sendNewNews() {
        log.info("Getting news which were not sent")
        val news = newsService.getNotSent().take(5)
        val trimmed = news.map { it.title }.toList()

        if (trimmed.isNotEmpty()) {
            log.info("Sending GPT message")
            val message = gptClient.generateNewsMessage(trimmed)

            tgBotClient.sendMessage(props.groupId, message + "\n\n@GlitchCryptoNews")

            news.forEach { it.isSent = true }
            newsService.saveAll(news)
        }
    }
}