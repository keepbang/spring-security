package com.keepbang.springsecurity.view

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/view")
class ViewController {

    @GetMapping("/main")
    fun main() = "main"

    @GetMapping("/user")
    fun user() = "user"

    @GetMapping("/admin")
    fun admin() = "admin"

    @GetMapping("/system-admin")
    fun systemAdmin() = "system-admin"

    @GetMapping("/paid-user")
    fun userPaid() = "user-paid"
}