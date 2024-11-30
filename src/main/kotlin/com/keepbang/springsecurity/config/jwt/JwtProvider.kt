package com.keepbang.springsecurity.config.jwt

import com.keepbang.springsecurity.config.AppProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(props: AppProperties) {
    private val secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(props.jwt.key))
    private val expiredHours: Long = props.jwt.expiredHours
    private val refreshExpired: Long = props.jwt.refreshExpired

    fun generateToken(userId: String, roles: List<String>): String {
        val now = Date()
        val expiredAt = Duration.ofHours(expiredHours)
        val key: SecretKey = secretKey

        return Jwts.builder()
            .header()
            .type("JWT")
            .and()
            .expiration(Date(now.time + expiredAt.toMillis()))
            .subject(userId)
            .claims(
                mapOf("roles" to roles)
            )
            .signWith(key)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val claims: Claims = getClaims(token)
        // `roles`를 SimpleGrantedAuthority로 변환
        val authorities = (claims["roles"] as List<*>)
            .map { SimpleGrantedAuthority(it.toString()) }

        return UsernamePasswordAuthenticationToken(
            claims.subject, token, authorities
        )
    }

    private fun getClaims(token: String): Claims {
        return getClaimsJws(token)
            .payload
    }

    private fun getClaimsJws(token: String): Jws<Claims> {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
    }

}

enum class TokenConst(
    val headerName: String
) {
    AUTHORIZATION_HEADER("Authorization"),
    REFRESH_HEADER("Authorization-refresh"),
    TOKEN_PREFIX("Bearer ");
}