package com.example.blog.dao.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
class Device(
    var serialNumber: String,
    var uuid: Long,
    var phoneNumber: String,
    var model: String,
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")
    var user: User?,
    @Id @GeneratedValue var id: Long? = null
)
