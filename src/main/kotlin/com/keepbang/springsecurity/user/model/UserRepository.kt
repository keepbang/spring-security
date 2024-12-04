package com.keepbang.springsecurity.user.model

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    @EntityGraph(ENTITY_GRAPH_USER_ROLES)
    fun findByLoginId(loginId: String): User?
}