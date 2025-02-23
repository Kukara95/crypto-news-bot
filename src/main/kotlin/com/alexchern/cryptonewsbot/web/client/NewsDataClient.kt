package com.alexchern.cryptonewsbot.web.client

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder

private const val BASE_URL = "https://newsdata.io/api/1"
private const val NEWS_PATH = "/latest"

@ConfigurationProperties(prefix = "news-data.client")
data class NewsDataClientProps(
    val apiKey: String
)

data class NewsDataDto(
    val title: String,
    val creator: String,
    val language: String
)

@Service
class NewsDataClient(
    private val props: NewsDataClientProps,
    @Qualifier("newsWebClient") private val newsWebClient: RestClient
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun getNews(): Set<NewsDataDto> {
        log.info("Getting news from $BASE_URL")
        return newsWebClient
            .get()
            .uri(createUrl())
            .retrieve()
            .onStatus(
                { it.value() == 429 },
                { _, _ ->
                    log.warn("Rate limit for news is exceeded")
                }
            )
            .body(JsonNode::class.java)!!
            .toNewsDto()
            .filter { it.language == "english" }
            .toSet()
    }

    private fun createUrl(): String {
        return UriComponentsBuilder
            .fromUriString(BASE_URL)
            .path(NEWS_PATH)
            .queryParam("apikey", props.apiKey)
            .queryParam("q", "crypto")
            .toUriString()
    }
}

fun JsonNode.toNewsDto(): Set<NewsDataDto> {
    if (isError(this)) {
        throw NewsException("news response was not successful")
    }

    val results = this["results"] as ArrayNode
    return results
        .map {
            NewsDataDto(
                it["title"].asText(),
                it["creator"].asText(),
                it["language"].asText()
            )
        }
        .toSet()
}

private fun isError(value: JsonNode) = value.has("error")
class NewsException(message: String) : RuntimeException(message)
