package com.keepbang.springsecurity.config

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties("app")
@Validated
data class AppProperties(
    @field:Valid
    val jwt: JwtProperties
)

data class JwtProperties(
    @field:NotBlank
    val key: String
)