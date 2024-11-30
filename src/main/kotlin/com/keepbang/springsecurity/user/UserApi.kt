package com.keepbang.springsecurity.user

import com.keepbang.springsecurity.user.dto.CreateUserDto
import com.keepbang.springsecurity.user.dto.UserDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UserApi(
    private val userService: UserService
) {

    @PostMapping("/singup")
    fun singUp(@RequestBody dto: CreateUserDto): ResponseEntity<UserDto> {
        return ResponseEntity.ok(
            userService.save(dto)
        )
    }

    @PutMapping("/admin/users/roles")
    fun updateRoles(

    ) {

    }

}

