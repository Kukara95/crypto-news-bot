package com.alexchern.cryptonewsbot.domain

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Document
data class News(
    val id: String? = null,
    @Indexed(unique = true)
    val title: String,
    var isSent: Boolean = false
)

@Repository
interface NewsRepository : MongoRepository<News, String> {
    @Query("{ 'isSent' : false }")
    fun getNotSentNews(): List<News>
}

@Service
@Transactional
class NewsService(private val repo: NewsRepository) {

    @Transactional(readOnly = true)
    fun getAll(): List<News> {
        return repo.findAll()
    }

    @Transactional(readOnly = true)
    fun getNotSent(): List<News> {
        return repo.getNotSentNews()
    }

    fun saveAll(news: Iterable<News>): List<News> {
        return repo.saveAll(news)
    }

    fun deleteAll() {
        repo.deleteAll()
    }
}
