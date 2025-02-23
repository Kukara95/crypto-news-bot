package com.alexchern.cryptonewsbot.domain.repository

import com.alexchern.cryptonewsbot.domain.model.News
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface NewsRepository : MongoRepository<News, String> {
    fun findByTitle(title: String): News
    fun existsByTitle(title: String): Boolean
}