package com.alexchern.cryptonewsbot.bot

import com.alexchern.cryptonewsbot.domain.Chat
import com.alexchern.cryptonewsbot.domain.ChatService
import com.alexchern.cryptonewsbot.domain.NewsService
import com.alexchern.cryptonewsbot.web.client.NewsClient
import com.alexchern.cryptonewsbot.web.client.TgBotClient
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@ConfigurationProperties(prefix = "tg.bot.listener")
data class TgBotProps(
    val delay: Long
)

@Component
class TgBot(
    private val newsClient: NewsClient,
    private val tgBotClient: TgBotClient,
    private val chatService: ChatService,
    private val newsService: NewsService,
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(fixedDelay = 1000)
    fun listenBotChat() {
//        tgBotClient.getUpdates().forEach { update ->
//            if (!chatService.existsByChatId(update.chatId)) {
//                val chat = Chat(chatId = update.chatId, lastUpdateId = update.lastUpdateId)
//                chatService.save(chat)
//            }
//        }
    }

    @Scheduled(fixedDelay = 10000)
    fun pepka() {
//        val newNews = mutableSetOf<String>()
//
//        log.info("Getting NEWS")
//        newsClient.getNews().forEach {
//            if (!newsService.existsByTitle(it.title)) {
//                log.info("Got new NEWS")
//                newNews.add(newsService.save(News(creator = it.creator, title = it.title)).title)
//            }
//        }
//
//        if (newNews.isNotEmpty()) {
//            val filters = filterService.getFilters().map { it.word }.toSet()
//            val result = hashSetOf<String>()
//
//            if (filters.isEmpty()) {
//                chatService.getAll().forEach { chat ->
//                    Thread.sleep(2000)
//                    tgBotClient.sendMessage(chat.chatId, newNews.joinToString("\n\n") + "\n\n@GlitchCryptoNews")
//                }
//            } else {
//                filters.forEach { filter ->
//                    newNews.forEach { new ->
//                        if (new.contains(filter)) {
//                            result.add(new)
//                        }
//                    }
//                }
//
//                chatService.getAll().forEach { chat ->
//                    Thread.sleep(2000)
//                    tgBotClient.sendMessage(chat.chatId, result.joinToString("\n\n") + "\n\n@GlitchCryptoNews")
//                }
//            }
//
//        }
    }
}