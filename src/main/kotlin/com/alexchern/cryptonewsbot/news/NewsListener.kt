package com.alexchern.cryptonewsbot.news

import com.alexchern.cryptonewsbot.domain.News
import com.alexchern.cryptonewsbot.domain.NewsService
import com.alexchern.cryptonewsbot.web.client.NewsApiClient
import com.alexchern.cryptonewsbot.web.client.NewsDataClient
import com.alexchern.cryptonewsbot.web.client.NewsDataDto
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class NewsListener(
    private val newsDataClient: NewsDataClient,
    private val newsApiClient: NewsApiClient,
    private val newsService: NewsService,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(fixedDelay = 180_000)
    fun listen() {
        val newsData = newsDataClient.getNews()
        val newsApi = newsApiClient.getNews()

        val newNews = getNewNews(newsData + newsApi)
        if (newNews.isNotEmpty()) {
            log.info("New news found")
            newsService.saveAll(newNews)
        }
    }

    @Scheduled(cron = "0 0 0 * * 0")
    fun cleanUp() {
        newsService.deleteAll()
    }

    private fun getNewNews(newsFromApi: Set<NewsDataDto>): Set<News> {
        val existingNews = newsService.getAll().map { it.title }.toSet()
        val newNews = newsFromApi.map { it.title }.toSet()

        return newNews.subtract(existingNews).map { News(title = it) }.toSet()
    }
}