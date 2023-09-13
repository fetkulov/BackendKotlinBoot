package com.example.blog.service

import com.example.blog.dao.DeviceRepository
import com.example.blog.dao.UserRepository
import com.example.blog.dao.entity.Device
import com.example.blog.dao.entity.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Service


@Service
class DeviceUserService(val deviceRepository: DeviceRepository, val userRepository: UserRepository) {
    fun findAllUsers(): Iterable<User> = userRepository.findAll()
    fun findAllDevices(): Iterable<Device> = deviceRepository.findAll()
    fun mapUserDevice(userId: Long, deviceId: Long): Device {
        val user = userRepository.findById(userId).orElseThrow { RuntimeException("No user with id=$userId found") }
        val device =
            deviceRepository.findById(deviceId).orElseThrow { RuntimeException("No device with id=$deviceId found") }
        device.user = user
        return deviceRepository.save(device)
    }
}