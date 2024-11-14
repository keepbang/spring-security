package com.keepbang.springsecurity.common.convert

interface CodedEnum<T> {
    fun getCode(): T;
}