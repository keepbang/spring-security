package com.keepbang.springsecurity.config.security

import com.keepbang.springsecurity.user.model.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler

@Configuration
@EnableWebSecurity(debug = true)
class SecurityConfig {

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
                    .requestMatchers("/api/singup", "/h2-console/**").permitAll()
                    .requestMatchers("/view/user").hasAuthority(Role.USER.getCode())
                    .requestMatchers("/view/system-admin").hasAuthority(Role.SYSTEM_ADMIN.getCode())
                    .requestMatchers("/view/paid-user").hasAuthority(Role.PAID_USER.getCode())
                    .requestMatchers("/view/admin").hasAuthority(Role.ADMIN.getCode())
                    .anyRequest().authenticated()
            }.formLogin {
                it.loginProcessingUrl("/login")
                    .usernameParameter("id")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/view/main")
            }.logout {
                it.logoutSuccessHandler(HttpStatusReturningLogoutSuccessHandler())
                    .logoutSuccessUrl("/login")
            }
            .headers {
                it.frameOptions { it.disable() }
            }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}