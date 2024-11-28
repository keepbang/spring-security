package com.keepbang.springsecurity.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing
@EnableConfigurationProperties(
    AppProperties::class
)
class AppConfig {
}