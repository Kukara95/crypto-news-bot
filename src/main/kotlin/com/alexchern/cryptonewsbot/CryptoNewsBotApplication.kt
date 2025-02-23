package com.alexchern.cryptonewsbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
class CryptoNewsBotApplication

fun main(args: Array<String>) {
    runApplication<CryptoNewsBotApplication>(*args)
}
