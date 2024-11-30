package com.keepbang.springsecurity.config

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class AppProperties(
    val jwt: JwtProperties
)

data class JwtProperties(
    val key: String,
    val expiredHours: Long,
    val refreshExpired: Long
)