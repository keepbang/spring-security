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
        http.csrf {
            it.disable()
        }.formLogin {
            it.loginProcessingUrl("/login")
                .usernameParameter("id")
                .passwordParameter("password")
        }.sessionManagement {
            it.disable()
        }.authorizeHttpRequests {
            it.requestMatchers("/api/singup").permitAll()
                .anyRequest().authenticated()
        }

        return http.build()
    }
}