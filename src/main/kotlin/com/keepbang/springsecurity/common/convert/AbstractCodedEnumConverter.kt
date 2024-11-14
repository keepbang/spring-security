package com.keepbang.springsecurity.common.convert

import jakarta.persistence.AttributeConverter
import java.util.*

abstract class AbstractCodedEnumConverter<T, E>(
    private val clazz: Class<T>
) :
    AttributeConverter<T, E> where T : Enum<T>, T : CodedEnum<E> {

    override fun convertToDatabaseColumn(attribute: T): E {
        return attribute.getCode()
    }

    override fun convertToEntityAttribute(dbData: E): T? {
        return dbData?.let {
            clazz.enumConstants
                .firstOrNull { it.getCode() == dbData }
                ?: throw IllegalArgumentException("Cannot convert ${dbData}")
        }

    }
}