package com.keepbang.springsecurity.user

import com.keepbang.springsecurity.user.dto.CreateUserDto
import com.keepbang.springsecurity.user.dto.UserDto
import com.keepbang.springsecurity.user.model.User
import com.keepbang.springsecurity.user.model.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) :UserDetailsService {
    @Transactional
    fun save(dto: CreateUserDto): UserDto {
        return userRepository.save(
            User(dto.loginId, passwordEncoder.encode(dto.password))
        ).let { UserDto(it.id!!, it.loginId, it.createdAt) }
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = (userRepository.findByLoginId(username)
            ?: throw UsernameNotFoundException(username))
        return org.springframework.security.core.userdetails.User
            .builder()
            .username(user.loginId)
            .password(user.password)
            .authorities(user.roles.map { SimpleGrantedAuthority(it) })
            .build()
    }

}