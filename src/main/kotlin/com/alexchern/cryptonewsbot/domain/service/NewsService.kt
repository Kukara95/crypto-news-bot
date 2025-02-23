package com.alexchern.cryptonewsbot.domain.service

import com.alexchern.cryptonewsbot.domain.model.News
import com.alexchern.cryptonewsbot.domain.repository.NewsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
@Transactional
class NewsService(private val repo: NewsRepository) {

    @Transactional(readOnly = true)
    fun getByTitle(title: String): News {
        return repo.findByTitle(title)
    }

    @Transactional(readOnly = true)
    fun existsByTitle(title: String): Boolean {
        return repo.existsByTitle(title)
    }

    fun save(news: News): News {
        return repo.save(news)
    }

    fun saveAll(news: Set<News>): List<News> {
        return repo.saveAll(news)
    }
}