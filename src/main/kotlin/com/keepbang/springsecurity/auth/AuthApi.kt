package com.keepbang.springsecurity.auth

import com.keepbang.springsecurity.auth.dto.LoginResponse
import com.keepbang.springsecurity.config.jwt.JwtProvider
import com.keepbang.springsecurity.config.security.LoginRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthApi(
    private val authenticationManager: AuthenticationManager,
    private val jwtProvider: JwtProvider
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<*> {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.id, request.password)
        )

        val token = jwtProvider.generateToken(
            authentication.getName(),
            authentication.getAuthorities()
                .map { it.authority }
        )

        return ResponseEntity.ok<Any>(LoginResponse(token))
    }

}