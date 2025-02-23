package com.alexchern.cryptonewsbot.domain.service

import com.alexchern.cryptonewsbot.domain.model.Chat
import com.alexchern.cryptonewsbot.domain.repository.ChatRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

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