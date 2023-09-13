package com.example.blog.dao

import com.example.blog.dao.entity.Device
import com.example.blog.dao.entity.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@DataJpaTest
class RepositoriesTests @Autowired constructor(
		val entityManager: TestEntityManager,
		val userRepository: UserRepository,
		val deviceRepository: DeviceRepository) {

	@Test
	fun `When findByIdOrNull then return Device`() {
		val johnSmith = User("John", "Smith", "address1", LocalDateTime.now())
		entityManager.persist(johnSmith)
		val device111 = Device("SR111", 123L, "+48001", "test model", johnSmith)
		entityManager.persist(device111)
		entityManager.flush()
		val foundDevice = deviceRepository.findByIdOrNull(device111.id!!)
		assertThat(foundDevice).isEqualTo(device111)
		assertThat(foundDevice!!.user).isEqualTo(johnSmith)
	}

	@Test
	fun `users test`(){
		val device111 = Device("SR111", 121L, "+48001", "test model", user = null)
		val device112 = Device("SR112", 122L, "+48002", "perfect model", user = null)
		entityManager.persist(device111)
		entityManager.persist(device112)
		val smith = "Smith"
		val devices = mutableSetOf(device111, device112)
		val johnSmith = User("John", smith, "address1", devices = devices)
		val loraPalmer = User("Lora", "Palmer", "south")
		entityManager.persist(johnSmith)
		entityManager.persist(loraPalmer)

		entityManager.flush()
		val users = userRepository.findAll()
		val john = users.first{it.lastName == smith}
		assertThat(john.devices.size).isEqualTo(devices.size)
		assertThat(john.devices).isEqualTo(devices)
	}


}
