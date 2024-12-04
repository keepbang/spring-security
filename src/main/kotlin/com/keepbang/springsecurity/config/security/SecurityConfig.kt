package com.keepbang.springsecurity.config.security

import com.keepbang.springsecurity.config.jwt.JwtProvider
import com.keepbang.springsecurity.config.jwt.TokenAuthenticationFilter
import com.keepbang.springsecurity.user.model.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtProvider: JwtProvider
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.httpBasic { it.disable() }
            .sessionManagement { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/favicon.ico",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/swagger/**",
                    ).permitAll()
                    .requestMatchers("/auth/login", "/api/singup", "/h2-console/**").permitAll()
                    .requestMatchers("/view/user").hasAuthority(Role.USER.getCode())
                    .requestMatchers("/view/system-admin").hasAuthority(Role.SYSTEM_ADMIN.getCode())
                    .requestMatchers("/view/paid-user").hasAuthority(Role.PAID_USER.getCode())
                    .requestMatchers("/view/admin").hasAuthority(Role.ADMIN.getCode())
                    .anyRequest().authenticated()
            }
            .logout { it.disable() }
            .formLogin { it.disable() }
            .headers {
                it.frameOptions { it.disable() }
            }.addFilterBefore(
                TokenAuthenticationFilter(jwtProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }

    @Bean
    fun authenticationManager(
        authenticationConfiguration: AuthenticationConfiguration
    ): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


}