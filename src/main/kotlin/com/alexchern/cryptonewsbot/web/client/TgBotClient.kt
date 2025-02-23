package com.alexchern.cryptonewsbot.web.client

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder

private const val BASE_URL = "https://api.telegram.org"
private const val UPDATES_PATH = "/getUpdates"
private const val SEND_PATH = "/sendMessage"

data class TgUpdateDto(
    val chatId: Long,
    val updateId: Long,
    val username: String,
    val message: String,
)

data class TgSendMessageRequest(
    @JsonProperty("chat_id")
    val chatId: Long,
    val text: String
)

@ConfigurationProperties(prefix = "tg.bot.client")
data class TgBotClientProps(
    val apiKey: String
)

@Service
class TgBotClient(
    private val props: TgBotClientProps,
    @Qualifier("tgBotWebClient") private val tgBotWebClient: RestClient
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun getUpdates(): List<TgUpdateDto> {
        log.info("Getting updates from TG API")
        val updatesJson = tgBotWebClient
            .get()
            .uri(createUrl(UPDATES_PATH))
            .retrieve()
            .body(JsonNode::class.java) ?: throw IllegalStateException("TG Updates body is null")

        return updatesJson.toTgUpdateDto()
    }

    fun sendMessage(chatId: Long, text: String): ResponseEntity<Void> {
        log.info("Sending message to TG chat with id $chatId")
        return tgBotWebClient
            .post()
            .uri(createUrl(SEND_PATH))
            .body(TgSendMessageRequest(chatId, text))
            .retrieve()
            .toBodilessEntity()
    }

    private fun createUrl(path: String): String {
        return UriComponentsBuilder
            .fromUriString(BASE_URL)
            .path(props.apiKey)
            .path(path)
            .toUriString()
    }
}

fun JsonNode.toTgUpdateDto(): List<TgUpdateDto> {
    if (isTgError(this)) {
        throw TgUpdatesException("tg update response was not successful")
    }

    val result = this["result"] as ArrayNode

    println("info, result: $result")
    return result.map { update ->
        val text = update["message"]

        if (text == null) {
            println("text is null")
            TgUpdateDto(
                chatId = 1123123131,
                username = "null",
                message = "null",
                updateId = 1
            )
        } else {
            val coolText = text["text"]
            TgUpdateDto(
                chatId = update["message"]["chat"]["id"].asLong(),
                username = update["message"]["from"]["username"].asText(),
                message = coolText.asText()
            )
        }
    }
}

private fun isTgError(value: JsonNode) = !value["ok"].asBoolean()
class TgUpdatesException(message: String) : RuntimeException(message)