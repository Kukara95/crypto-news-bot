package com.alexchern.cryptonewsbot.domain.repository

import com.alexchern.cryptonewsbot.domain.model.Chat
import com.alexchern.cryptonewsbot.domain.model.Filter
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : MongoRepository<Chat, String> {
    fun findByChatId(chatId: Long): Chat
    fun existsByChatId(chatId: Long): Boolean
}

@Repository
interface FilterRepository : MongoRepository<Filter, String>