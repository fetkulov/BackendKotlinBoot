package com.example.blog.controller

import com.example.blog.dao.UserRepository
import com.example.blog.dao.entity.User
import com.example.blog.service.DeviceUserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(private val deviceUserService: DeviceUserService) {

    @GetMapping("/")
    fun findAll(): Iterable<User> = deviceUserService.findAllUsers()


}