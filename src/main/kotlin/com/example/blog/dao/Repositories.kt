package com.example.blog.dao

import com.example.blog.dao.entity.Device
import com.example.blog.dao.entity.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param


interface DeviceRepository : CrudRepository<Device, Long> {
	fun findBySerialNumber(serialNumber: String): Device?
}

interface UserRepository : CrudRepository<User, Long> {
}

