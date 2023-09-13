package com.example.blog

import com.example.blog.controller.dto.Message
import com.example.blog.dao.entity.Device
import com.example.blog.dao.entity.User
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity
import kotlin.test.assertFailsWith


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests(@Autowired val restTemplate: TestRestTemplate) {

    @BeforeAll
    fun setup() {
        println(">> Start integration test")
    }

    @Test
    fun `Map device to User`() {
        println(">> Start Map device to User")
        val responseDevice: ResponseEntity<Array<Device>> = restTemplate.getForEntity(
            "/api/device/",
            Array<Device>::class.java
        )
        val devices: Array<Device> = responseDevice.body!!
        assertThat(devices.size).isEqualTo(3)
        val responseUsers: ResponseEntity<Array<User>> = restTemplate.getForEntity(
            "/api/user/",
            Array<User>::class.java
        )
        val users: Array<User> = responseUsers.body!!
        assertThat(users.size).isEqualTo(2)
        val userNameToMap = "Peter"
        val userIdToMap = users.first { it.firstName == userNameToMap }.id!!
        val deviceIdToMap = devices.first { it.user == null }.id!!
        val result = restTemplate.postForEntity(
            "/api/device/",
            Message(userIdToMap, deviceIdToMap),
            Device::class.java
        )
        assertThat(result).isNotNull
        val responseDeviceUpdated: ResponseEntity<Array<Device>> = restTemplate.getForEntity(
            "/api/device/",
            Array<Device>::class.java
        )
        val updatedDevices = responseDeviceUpdated.body!!
        val updatedDevice = updatedDevices.first { it.id == deviceIdToMap }
		updatedDevice.user?.let{
            Assertions.assertNotNull(it)
            Assertions.assertEquals(it.firstName, userNameToMap)
        }

    }

    @Test
    fun `Map device to User Fail`() {
        assertFailsWith<RuntimeException> {
            restTemplate.postForEntity(
                "/api/device/",
                Message(13, 13),
                Device::class.java
            )
        }
    }

    @AfterAll
    fun teardown() {
        println(">> Finish Integration test")
    }

}