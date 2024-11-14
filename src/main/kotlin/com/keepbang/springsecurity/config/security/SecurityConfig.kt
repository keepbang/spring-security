package com.keepbang.springsecurity.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
            it.requestMatchers("/api/singup").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
        }.formLogin {
            it.loginProcessingUrl("/login")
                .usernameParameter("id")
                .passwordParameter("password")
        }.sessionManagement {
            it.disable()
        }.csrf {
            it.disable()
        }.headers {
            it.frameOptions { it.disable() }
        }

        return http.build()
    }
}