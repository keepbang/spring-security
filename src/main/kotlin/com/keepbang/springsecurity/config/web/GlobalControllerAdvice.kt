package com.keepbang.springsecurity.config.web

import com.keepbang.springsecurity.common.exception.UnauthenticatedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalControllerAdvice {

    @ExceptionHandler(UnauthenticatedException::class)
    fun handleUnauthenticatedException(e: UnauthenticatedException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse.create(
                e,
                HttpStatus.UNAUTHORIZED,
                "Unauthorized Error"
            ), HttpStatus.UNAUTHORIZED
        )
    }
}