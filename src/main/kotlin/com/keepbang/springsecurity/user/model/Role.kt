package com.keepbang.springsecurity.user.model

import com.keepbang.springsecurity.common.convert.AbstractCodedEnumConverter
import com.keepbang.springsecurity.common.convert.CodedEnum
import jakarta.persistence.Converter

enum class Role(
    private val code: String,
    private val codeName: String
): CodedEnum<String> {
    SYSTEM_ADMIN("system-admin", "시스템 관리자"),
    ADMIN("admin", "관리자"),
    PAID_USER("paid-user", "과금 사용자"),
    USER("user", "사용자")
    ;

    override fun getCode() = this.code

}

@Converter(autoApply = true)
class RoleConverter : AbstractCodedEnumConverter<Role, String>(Role::class.java)