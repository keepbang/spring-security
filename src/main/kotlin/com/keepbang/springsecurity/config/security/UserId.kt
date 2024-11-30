package com.keepbang.springsecurity.config.security

import io.swagger.v3.oas.annotations.Parameter


@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Parameter(hidden = true)
annotation class UserId
