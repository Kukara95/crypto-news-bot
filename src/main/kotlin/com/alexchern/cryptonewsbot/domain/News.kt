package com.alexchern.cryptonewsbot.domain

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Document
data class News(
    val id: String? = null,
    val creator: String,
    @Indexed(unique = true)
    val title: String
)

@Repository
interface NewsRepository : MongoRepository<News, String> {
    fun existsByTitle(title: String): Boolean
}

@Service
@Transactional
class NewsService(private val repo: NewsRepository) {

    @Transactional(readOnly = true)
    fun getAll(): List<News> {
        return repo.findAll()
    }

    @Transactional(readOnly = true)
    fun existsByTitle(title: String): Boolean {
        return repo.existsByTitle(title)
    }

    fun save(news: News): News {
        return repo.save(news)
    }
}
