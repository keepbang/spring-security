package com.keepbang.springsecurity.user

import com.keepbang.springsecurity.common.model.AutoLongKeyBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @Column
    val loginId: String,
    @Column
    private var password: String
) : AutoLongKeyBaseEntity() {

    fun password(): String = password

}