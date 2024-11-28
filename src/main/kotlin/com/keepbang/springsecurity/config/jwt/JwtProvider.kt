package com.keepbang.springsecurity.config.jwt

import com.keepbang.springsecurity.config.AppProperties
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key

@Component
class JwtProvider(props: AppProperties) {

    private val key: Key = Keys.hmacShaKeyFor(props.jwt.key.toByteArray(StandardCharsets.UTF_8))

}