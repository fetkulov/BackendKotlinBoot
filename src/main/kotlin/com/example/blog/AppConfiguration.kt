package com.example.blog

import com.example.blog.dao.DeviceRepository
import com.example.blog.dao.UserRepository
import com.example.blog.dao.entity.Device
import com.example.blog.dao.entity.User
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

@Configuration
class AppConfiguration {

    @Bean
    fun databaseInitializer(
        userRepository: UserRepository,
        deviceRepository: DeviceRepository
    ) = ApplicationRunner {

        val johnSmith = userRepository.save(User("John", "Smith", "address1", LocalDateTime.now()))
        deviceRepository.save(Device("SR111", 123L, "+48001", "test model", johnSmith))
        deviceRepository.save(Device("SR114", 124L, "+48005", "new model", johnSmith))
        deviceRepository.save(Device("SR117", 127L, "+48007", "rare model", null))
        userRepository.save(User("Peter", "Pan", "island#4", LocalDateTime.now()))

    }
}
