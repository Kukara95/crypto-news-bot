package com.alexchern.cryptonewsbot.bot

import com.alexchern.cryptonewsbot.domain.ChatService
import com.alexchern.cryptonewsbot.domain.NewsService
import com.alexchern.cryptonewsbot.web.client.NewsClient
import com.alexchern.cryptonewsbot.web.client.TgBotClient
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.text
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "tg.bot")
data class TgBotProps(
    val apiKey: String
)

@Component
class TgBot(
    private val props: TgBotProps,
    private val newsClient: NewsClient,
    private val chatService: ChatService,
    private val newsService: NewsService,
) : CommandLineRunner {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun run(vararg args: String?) {
        val bot = bot {
            token = props.apiKey
            dispatch {
                text {

                }
            }
        }
    }

}