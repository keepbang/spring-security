package com.keepbang.springsecurity.auth

import com.keepbang.springsecurity.auth.dto.LoginResponse
import com.keepbang.springsecurity.config.jwt.JwtProvider
import com.keepbang.springsecurity.config.jwt.TokenConst
import com.keepbang.springsecurity.config.jwt.TokenType
import com.keepbang.springsecurity.config.security.LoginRequest
import com.keepbang.springsecurity.user.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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
    private val jwtProvider: JwtProvider,
    private val userService: UserService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.id, request.password)
        )

        val token = jwtProvider.generateToken(
            userId = authentication.name,
            tokenType = TokenType.ACCESS_TOKEN,
            roles = authentication.authorities
                .map { it.authority }
        )

        val refreshToken = jwtProvider.generateRefreshToken(authentication.name)

        return ResponseEntity.ok(LoginResponse(token, refreshToken))
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/token/reissue")
    fun reissueToken(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<LoginResponse> {
        val refreshToken: String = request.getHeader(TokenConst.REFRESH_HEADER.headerName)

        val claims = jwtProvider.getClaims(refreshToken);

        val userId = claims.subject
        val tokenType = claims["typ"] as TokenType

        jwtProvider.validationToken(tokenType, TokenType.REFRESH_TOKEN)

        val userDetails = userService.loadUserByUsername(userId)

        val token = jwtProvider.generateToken(
            userId = userDetails.username,
            tokenType = TokenType.ACCESS_TOKEN,
            roles = userDetails.authorities
                .map { it.authority }
        )

        val newRefreshToken = jwtProvider.generateRefreshToken(userDetails.username)

        return ResponseEntity.ok(LoginResponse(token, newRefreshToken))
    }

}