package com.keepbang.springsecurity.user.model

import com.keepbang.springsecurity.common.model.AutoLongKeyBaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

const val ENTITY_GRAPH_USER_ROLES = "User.roles"

@Entity
@NamedEntityGraph(
    name = ENTITY_GRAPH_USER_ROLES,
    attributeNodes = [
        NamedAttributeNode("_roles"),
    ]
)
@Table(name = "users")
class User(
    @Column(nullable = false, unique = true)
    val loginId: String,
    password: String
) : AutoLongKeyBaseEntity() {

    @Column(name = "password", nullable = false)
    var password: String = password
        private set

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    @Fetch(FetchMode.JOIN)
    private var  _roles: MutableSet<UserAuthority> = mutableSetOf(
        UserAuthority(
            this, Role.USER
        )
    )
    val roles: Set<String>
        get() = this._roles
            .map { it.role.getCode() }
            .toSet()


    fun updateRole() {

    }

}