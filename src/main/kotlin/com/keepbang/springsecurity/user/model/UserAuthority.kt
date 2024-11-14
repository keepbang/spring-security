package com.keepbang.springsecurity.user.model

import jakarta.persistence.*

@Entity
@Table(name = "user_authority")
class UserAuthority(
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        updatable = false
    )
    val user: User,

    @Column(name = "role", nullable = false, updatable = false)
    val role: Role,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {

}