package com.example.blog.dao.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
class User(
    var firstName: String,
    var lastName: String,
    var address: String,
    var birthday: LocalDateTime? = null,
    @JsonManagedReference
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "user")
    var devices: MutableSet<Device> = mutableSetOf(),
    @Id @GeneratedValue var id: Long? = null
)

