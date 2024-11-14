package com.keepbang.springsecurity.user

import com.keepbang.springsecurity.user.dto.CreateUserDto
import com.keepbang.springsecurity.user.dto.UserDto
import com.keepbang.springsecurity.user.model.User
import com.keepbang.springsecurity.user.model.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun create(dto: CreateUserDto): UserDto {
        return userRepository.save(
            User(dto.loginId, dto.password)
        ).let { UserDto(it.id!!, it.loginId, it.createdAt) }
    }

}