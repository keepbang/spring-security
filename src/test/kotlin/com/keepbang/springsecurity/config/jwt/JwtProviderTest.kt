package com.keepbang.springsecurity.config.jwt

import com.keepbang.springsecurity.config.AppProperties
import com.keepbang.springsecurity.config.JwtProperties
import io.jsonwebtoken.security.Keys
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey

class JwtProviderTest {

    val jwtProvider: JwtProvider = JwtProvider(
        AppProperties(
            JwtProperties(
                "8sknjlO3NdTBqo319DHLnQSqafRJEdKsETOdsDHLnQA2",
                3,
                100000000
            )
        )
    )

    @Test
    fun `jwt key 만들기 테스트`() {
        // given
        val key = "8sknjlO3NdTBqo319DHLnQSqafRJEdKsETOdsDHLnQA2";

        // when
        val secretKey: SecretKey = Keys.hmacShaKeyFor(key.toByteArray(StandardCharsets.UTF_8))

        // then
        assertThat(secretKey).isNotNull
    }

    @Test
    fun `token generate test`() {
        // given
        val userId = "userId"
        val roles = mutableSetOf(SimpleGrantedAuthority("ROLE_USER"))

        // when
        val token = jwtProvider.generateToken(userId = userId, roles = roles)

        // then
        assertThat(token).isNotNull
        val authentication: Authentication = jwtProvider.getAuthentication(token)
        assertThat(authentication.principal).isEqualTo(userId)
    }
}