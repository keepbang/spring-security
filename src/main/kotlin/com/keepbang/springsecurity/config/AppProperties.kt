package com.keepbang.springsecurity.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class AppProperties(
    val jwt: JwtProperties
)

data class JwtProperties(
    val key: String
)