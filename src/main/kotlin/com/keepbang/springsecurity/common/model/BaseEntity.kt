package com.keepbang.springsecurity.common.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    createdAt: LocalDateTime = LocalDateTime.now(),
    modifiedAt: LocalDateTime? = null
) {
    @CreatedDate
    @Column(name = "created_at", nullable = false,updatable = false)
    var createdAt: LocalDateTime = createdAt

    @LastModifiedDate
    var modifiedAt: LocalDateTime? = modifiedAt
        protected set

}

@MappedSuperclass
abstract class AutoLongKeyBaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) : BaseEntity()