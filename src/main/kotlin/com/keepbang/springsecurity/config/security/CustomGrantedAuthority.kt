package com.keepbang.springsecurity.config.security

import com.keepbang.springsecurity.user.model.Role
import org.springframework.security.core.GrantedAuthority

data class CustomGrantedAuthority(
    val role: Role
): GrantedAuthority {
    override fun getAuthority(): String = role.getCode()
}