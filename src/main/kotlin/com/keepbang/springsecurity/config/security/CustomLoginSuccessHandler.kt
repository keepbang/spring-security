package com.keepbang.springsecurity.config.security

import com.keepbang.springsecurity.common.exception.UnauthenticatedException
import com.keepbang.springsecurity.config.jwt.JwtProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

class CustomLoginSuccessHandler(
    private val jwtProvider: JwtProvider
): AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val principal = authentication.principal

        val (id, roles) = when (principal) {
            is UserDetails -> principal.username to principal.authorities
            else -> throw UnauthenticatedException("아직 지원하지 않는 로그인 방식입니다.")
        }

        println("id = ${id}")
        println("roles = ${roles}")

        val generateToken = jwtProvider.generateToken(id, roles.map { it.authority })


    }
}