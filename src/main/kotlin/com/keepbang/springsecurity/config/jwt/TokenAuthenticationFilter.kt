package com.keepbang.springsecurity.config.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

class TokenAuthenticationFilter(
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {


        // 요청 헤더의 Authorization 키의 값 조회
        val authorizationHeader = request.getHeader(TokenConst.AUTHORIZATION_HEADER.headerName)


        // 가져온 값에서 접두사 제거
        val oToken: Optional<String> = getAccessToken(authorizationHeader)

        oToken.ifPresent { token: String ->
            // 토큰 검증 및 authentication 생성
            val authentication: Authentication = jwtProvider.getAuthentication(token)
            //authentication 정보 security에 저장
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun getAccessToken(authorizationHeader: String?): Optional<String> {
        if (authorizationHeader != null
            && authorizationHeader.startsWith(TokenConst.TOKEN_PREFIX.headerName)
        ) {
            return Optional.of<String>(
                authorizationHeader.substring(
                    TokenConst.TOKEN_PREFIX.headerName.length
                )
            )
        }
        return Optional.empty()
    }
}