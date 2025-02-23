package com.alexchern.cryptonewsbot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration(proxyBeanMethods = false)
class WebConfig {

    @Bean("newsWebClient")
    fun newsWebClient(): RestClient {
        return RestClient.builder().build()
    }

    @Bean("newsApiRestClient")
    fun newsApiRestClient(): RestClient {
        return RestClient.builder().build()
    }

    @Bean("tgBotWebClient")
    fun tgBotWebClient(): RestClient {
        return RestClient.builder().build()
    }
}