package com.keepbang.springsecurity.user.model

import com.keepbang.springsecurity.common.model.AutoLongKeyBaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

@Entity
@Table(name = "users")
class User(
    @Column(nullable = false, unique = true)
    val loginId: String,
    password: String
) : AutoLongKeyBaseEntity() {

    @Column( name = "password", nullable = false)
    var password: String = password
        private set

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "user",
    )
    @Fetch(FetchMode.JOIN)
    val roles: Set<UserAuthority> = emptySet()




}