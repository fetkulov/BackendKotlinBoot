package com.example.blog.controller

import com.example.blog.dao.entity.Device
import com.example.blog.dao.entity.User
import com.example.blog.service.DeviceUserService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime
import java.util.*


@WebMvcTest
class HttpControllersTests(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var deviceUserService: DeviceUserService

    private val mapper = jacksonObjectMapper()

    @Test
    fun `List devices with user`() {
        val dev111 = Device("SR111", 123L, "+48001", "test model", null)
        val dev114 = Device("SR114", 124L, "+48005", "new model", null)

        every { deviceUserService.findAllDevices() } returns listOf(dev111, dev114)
        mockMvc.perform(get("/api/device/").accept(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].serialNumber").value(dev111.serialNumber))
            .andExpect(jsonPath("\$.[0].phoneNumber").value(dev111.phoneNumber))
            .andExpect(jsonPath("\$.[0].uuid").value(dev111.uuid))
            .andExpect(jsonPath("\$.[0].model").value(dev111.model))
            .andExpect(jsonPath("\$.[1].serialNumber").value(dev114.serialNumber))
            .andExpect(jsonPath("\$.[1].phoneNumber").value(dev114.phoneNumber))
            .andExpect(jsonPath("\$.[1].uuid").value(dev114.uuid))
            .andExpect(jsonPath("\$.[1].model").value(dev114.model))
    }

    @Test
    fun `List users`() {
        val serialNumber1 = "SR111"
        val dev111Model = "test model"
        val dev111 = Device(serialNumber1, 121L, "+48001", dev111Model, null)
        val serialNumber2 = "SR112"
        val dev112Model = "last model"
        val dev112 = Device(serialNumber2, 122L, "+48002", dev112Model, null)
        val john = "John"
        val johnSmith = User(john, "Smith", "address1",  devices = mutableSetOf(dev111, dev112))
        val doraSmith = User("Dora", "Smith", "address1",)
        every { deviceUserService.findAllUsers() } returns listOf(johnSmith, doraSmith)
        val result = mockMvc.perform(get("/api/user/").accept(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].firstName").value(johnSmith.firstName))
            .andExpect(jsonPath("\$.[1].firstName").value(doraSmith.firstName))
            .andReturn()
        val users = mapper.readValue(result.response.contentAsString, Array<User>::class.java) //todo: add support for LocalDateTime
        val johnDevices = users.first { it.firstName == john }.devices
        assertThat(johnDevices.size).isEqualTo(2)
        assertThat(johnDevices.first{it.serialNumber == serialNumber1}.model).isEqualTo(dev111Model)
        assertThat(johnDevices.first{it.serialNumber == serialNumber2}.model).isEqualTo(dev112Model)
    }


    @Test
    fun `Add device`() {
        val userId = 12L
        val merryP = User("Merry", "P", "Parkline#1", LocalDateTime.now(), id = userId)
        val deviceId = 123L
        val dev111 = Device("SR111", 111L, "+48001", "test model", merryP, id = deviceId)
        every { deviceUserService.mapUserDevice(userId, deviceId) } returns dev111
        mockMvc.perform(
            post("/api/device/").accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content("{ \"userId\": $userId, \"deviceId\": $deviceId}")
                .accept(APPLICATION_JSON)
        )
            .andExpect(status().isOk())
    }

    @Test
    fun `Incorrect Input`() {
        val result = mockMvc.perform(
            post("/api/device/").accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content("{ \"deviceId\":123}")
        )
            .andExpect(status().isBadRequest())
            .andReturn()

        val jsonResult = result.response.contentAsString
        val errors = Arrays.stream(mapper.readValue(jsonResult, Array<String>::class.java)).toList()

        val errorsAmount = 1
        assertThat(errors.size).isEqualTo(errorsAmount)
        assertThat(errors[0])?.isEqualTo("userId value must be positive")

    }
}
