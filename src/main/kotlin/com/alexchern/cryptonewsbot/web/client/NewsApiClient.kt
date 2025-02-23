package com.alexchern.cryptonewsbot.web.client

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDate

private const val BASE_URL = "https://newsapi.org/v2/"
private const val NEWS_PATH = "/everything"

@ConfigurationProperties(prefix = "news-api.client")
data class NewsApiClientProps(
    val apiKey: String
)

@Service
class NewsApiClient(
    private val props: NewsApiClientProps,
    @Qualifier("newsApiRestClient") private val newsWebClient: RestClient
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    fun getNews(): Set<NewsDataDto> {
        val url = createUrl()
        log.info("Getting news from $url")
        return newsWebClient
            .get()
            .uri(url)
            .retrieve()
            .onStatus(
                { it.value() == 429 },
                { _, _ ->
                    log.warn("Rate limit for news api is exceeded")
                }
            )
            .body(JsonNode::class.java)!!
            .toNewsApiDto()
    }

    private fun createUrl(): String {
        return UriComponentsBuilder
            .fromUriString(BASE_URL)
            .path(NEWS_PATH)
            .queryParam("apiKey", props.apiKey)
            .queryParam("q", "crypto")
            .queryParam("language", "en")
            .queryParam("from", LocalDate.now().minusDays(1).toString())
            .toUriString()
    }
}

fun JsonNode.toNewsApiDto(): Set<NewsDataDto> {
    if (isError(this)) {
        throw NewsApiException("news api response was not successful")
    }

    val results = this["articles"] as ArrayNode
    return results
        .map {
            NewsDataDto(
                it["title"].asText(),
                it["content"].asText(),
                "english"
            )
        }
        .toSet()
}

private fun isError(value: JsonNode) = value.get("status").asText() != "ok"
class NewsApiException(message: String) : RuntimeException(message)