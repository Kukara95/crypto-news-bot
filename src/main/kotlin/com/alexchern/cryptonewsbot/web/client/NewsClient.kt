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

@ConfigurationProperties(prefix = "news.client")
data class NewsClientProps(
    val apiKey: String
)

data class NewsDto(
    val title: String,
    val creator: String,
    val language: String
)

@Service
class NewsClient(
    private val props: NewsClientProps,
    @Qualifier("newsWebClient") private val newsWebClient: RestClient
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    fun getNews(): List<NewsDto> {
        return newsWebClient
            .get()
            .uri(createUrl(BASE_URL, "crypto", NEWS_PATH))
            .retrieve()
            .body(JsonNode::class.java)!!
            .toNewsDto()
            .filter { it.language == "english" }
    }

    private fun createUrl(baseUrl: String, title: String, vararg path: String): String {
        return UriComponentsBuilder
            .fromUriString(baseUrl)
            .path(path.joinToString(separator = ""))
            .queryParam("apikey", props.apiKey)
            .queryParam("q", title)
            .toUriString()
    }
}

fun JsonNode.toNewsDto(): HashSet<NewsDto> {
    if (isError(this)) {
        throw NewsException("news response was not successful")
    }

    val results = this["results"] as ArrayNode
    return HashSet(
        results
            .map {
                NewsDto(
                    it["title"].asText(),
                    it["creator"].asText(),
                    it["language"].asText()
                )
            }
        .toSet())
}

private fun isError(value: JsonNode) = value.has("error")
class NewsException(message: String) : RuntimeException(message)
