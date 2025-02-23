package com.alexchern.cryptonewsbot.web.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode

data class TgUpdateResponse(
    val chatId: Long,
    val lastUpdateId: Long,
    val username: String,
    val message: String,
)

data class TgSendMessageRequest(
    @JsonProperty("chat_id")
    val chatId: Long,
    val text: String
)

fun JsonNode.toTgUpdateDto(): List<TgUpdateResponse> {
    if (isTgError(this)) {
        throw TgUpdatesException("Got error response from TG Updates")
    }

    val result = this["result"] as ArrayNode
    return listOf()
//    return result.map { update ->
//        val text = update["message"]
//
//        if (text == null) {
//            println("text is null")
//            TgUpdateResponse(
//                chatId = 1123123131,
//                username = "null",
//                message = "null",
//                lastUpdateId = 1
//            )
//        } else {
//            val coolText = text["text"]
//            TgUpdateResponse(
//                chatId = update["message"]["chat"]["id"].asLong(),
//                username = update["message"]["from"]["username"].asText(),
//                message = coolText.asText()
//            )
//        }
//    }
}

private fun isTgError(value: JsonNode) = !value["ok"].asBoolean()
class TgUpdatesException(message: String) : RuntimeException(message)
