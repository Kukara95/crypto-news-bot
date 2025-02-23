package com.alexchern.cryptonewsbot.domain.model

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class News(
    val id: String? = null,
    val creator: String,
    @Indexed(unique = true)
    val title: String
)