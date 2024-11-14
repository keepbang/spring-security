package com.keepbang.springsecurity.user.dto

import java.time.LocalDateTime

data class UserDto(
    val id: Long,
    val loginId: String,
    val createdAt: LocalDateTime
)