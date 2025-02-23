package com.alexchern.cryptonewsbot.web.client

import com.alexchern.cryptonewsbot.bot.TgBotProps
import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder

private const val BASE_URL = "https://api.telegram.org"
private const val SEND_PATH = "/sendMessage"

data class TgSendMessageRequest(
    @JsonProperty("chat_id")
    val chatId: Long,
    val text: String
)

@Service
class TgBotClient(
    private val props: TgBotProps,
    @Qualifier("tgBotWebClient") private val tgBotWebClient: RestClient
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun sendMessage(chatId: Long, text: String): ResponseEntity<Void> {
        log.info("Sending message to TG chat with id $chatId")
        return tgBotWebClient
            .post()
            .uri(createUrl())
            .body(TgSendMessageRequest(chatId, text))
            .retrieve()
            .toBodilessEntity()
    }

    private fun createUrl(): String {
        return UriComponentsBuilder
            .fromUriString(BASE_URL)
            .path(props.apiKey)
            .path(SEND_PATH)
            .toUriString()
    }
}