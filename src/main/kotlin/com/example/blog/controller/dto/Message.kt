package com.example.blog.controller.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull


class Message(
    @field:Min(value = 1L, message = "userId value must be positive")
    val userId: Long,
    @field:Min(value = 1L, message = "serialNumber value must be positive")
    val deviceId: Long
)