package com.alexchern.cryptonewsbot.bot

import com.alexchern.cryptonewsbot.domain.model.Chat
import com.alexchern.cryptonewsbot.domain.model.Filter
import com.alexchern.cryptonewsbot.domain.model.News
import com.alexchern.cryptonewsbot.domain.service.ChatService
import com.alexchern.cryptonewsbot.domain.service.FilterService
import com.alexchern.cryptonewsbot.domain.service.NewsService
import com.alexchern.cryptonewsbot.web.client.NewsClient
import com.alexchern.cryptonewsbot.web.client.TgBotClient
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@ConfigurationProperties(prefix = "tg.bot.filter")
data class TgBotProps(
    val words: List<String>,
)

@Component
class TgBot(
    private val newsClient: NewsClient,
    private val tgBotClient: TgBotClient,
    private val chatService: ChatService,
    private val newsService: NewsService,
    private val filterService: FilterService,
) {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val objectMapper = jacksonObjectMapper()

    @Scheduled(fixedDelay = 500)
    fun saveChats() {
        tgBotClient.getUpdates().forEach { update ->
            if (!chatService.existsByChatId(update.chatId)) {
                chatService.save(Chat(chatId = update.chatId))
            }

            if (update.message == "/start") {
                Thread.sleep(1500)
                tgBotClient.sendMessage(update.chatId, "Hello, to set new filters send /setFilters and then filter words, to see existing send /listFilters")
            }

            if (update.message == "/setFilters") {
                update.message.replace("/setFilters", "").trim()
                val words = update.message.split(",")
                filterService.deleteAll()
                words.forEach { word -> filterService.save(Filter(word = word.lowercase(Locale.getDefault()))) }
            }

            if (update.message == "/listFilters") {
                Thread.sleep(1500)
                tgBotClient.sendMessage(update.chatId, filterService.getFilters().joinToString("\n"))
            }
        }
    }

    @Scheduled(fixedDelay = 10000)
    fun pepka() {
        val newNews = mutableSetOf<String>()

        log.info("Getting NEWS")
        newsClient.getNews().forEach {
            if (!newsService.existsByTitle(it.title)) {
                log.info("Got new NEWS")
                newNews.add(newsService.save(News(creator = it.creator, title = it.title)).title)
            }
        }

        if (newNews.isNotEmpty()) {
            val filters = filterService.getFilters().map { it.word }.toSet()
            val result = hashSetOf<String>()

            if (filters.isEmpty()) {
                chatService.getAll().forEach { chat ->
                    Thread.sleep(2000)
                    tgBotClient.sendMessage(chat.chatId, newNews.joinToString("\n\n") + "\n\n@GlitchCryptoNews")
                }
            } else {
                filters.forEach { filter ->
                    newNews.forEach { new ->
                        if (new.contains(filter)) {
                            result.add(new)
                        }
                    }
                }

                chatService.getAll().forEach { chat ->
                    Thread.sleep(2000)
                    tgBotClient.sendMessage(chat.chatId, result.joinToString("\n\n") + "\n\n@GlitchCryptoNews")
                }
            }

        }
    }
}