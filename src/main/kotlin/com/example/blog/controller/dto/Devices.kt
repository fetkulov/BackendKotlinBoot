package com.example.blog.controller.dto

import com.example.blog.dao.entity.Device
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Devices(
    var devices: List<Device> = ArrayList()
)