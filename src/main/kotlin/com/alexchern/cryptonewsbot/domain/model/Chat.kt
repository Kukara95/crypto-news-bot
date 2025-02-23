package com.alexchern.cryptonewsbot.domain.model

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Chat(
    val id: String? = null,
    @Indexed(unique = true)
    val chatId: Long
)

@Document
data class Filter(
    val id: String? = null,
    val word: String
)