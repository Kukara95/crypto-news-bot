package com.alexchern.cryptonewsbot.domain

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Document
data class Chat(
    val id: String? = null,
    @Indexed(unique = true)
    val chatId: Long,
    @Indexed
    val lastUpdateId: Long
)

@Repository
interface ChatRepository : MongoRepository<Chat, String> {
    fun findByChatId(chatId: Long): Chat
    fun existsByChatId(chatId: Long): Boolean
}

@Service
@Transactional
class ChatService(private val repo: ChatRepository) {

    @Transactional(readOnly = true)
    fun getByChatId(chatId: Long): Chat {
        return repo.findByChatId(chatId)
    }

    @Transactional(readOnly = true)
    fun getAll(): List<Chat> {
        return repo.findAll()
    }

    @Transactional(readOnly = true)
    fun existsByChatId(chatId: Long): Boolean {
        return repo.existsByChatId(chatId)
    }

    fun save(chat: Chat): Chat {
        return repo.save(chat)
    }
}
