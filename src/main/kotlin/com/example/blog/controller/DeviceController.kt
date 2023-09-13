package com.example.blog.controller

import com.example.blog.controller.dto.Message
import com.example.blog.dao.entity.Device
import com.example.blog.service.DeviceUserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/device")
class DeviceController(private val deviceUserService: DeviceUserService) {

    @GetMapping("/")
    fun findAll(): Iterable<Device> = deviceUserService.findAllDevices()

    @PostMapping("/")
    fun update(@Valid @RequestBody message: Message):ResponseEntity<Device> {
        val result: Device = deviceUserService.mapUserDevice(message.userId, message.deviceId)
        return ResponseEntity.ok(result)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): MutableList<String> {
        return ex.bindingResult
            .allErrors
            .stream()
            .map<String> { it.defaultMessage}
            .toList()
    }

}