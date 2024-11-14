package com.keepbang.springsecurity.user.dto

data class CreateUserDto(
    val loginId: String,
    val password: String
)