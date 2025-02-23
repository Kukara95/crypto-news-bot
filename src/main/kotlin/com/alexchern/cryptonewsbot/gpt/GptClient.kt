package com.alexchern.cryptonewsbot.gpt

import org.slf4j.LoggerFactory
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.stereotype.Service

@Service
class GptClient(
    private val chatModel: OpenAiChatModel
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    fun generateNewsMessage(news: List<String>): String {
        val message = "Use these news tittles and create a Telegram channel post, post should be short and don't add # to the end of the message. Keep it short and sweet, ensure output is informative with no clickbait info \nNews:\n$news"
        log.info("Calling GPT API with message:\n$message")

        val call = chatModel.call(
            Prompt(
                message,
                OpenAiChatOptions.builder()
                    .model("gpt-4o")
                    .temperature(0.7)
                    .build()
            )
        )

        return call.result.output.text
    }
}