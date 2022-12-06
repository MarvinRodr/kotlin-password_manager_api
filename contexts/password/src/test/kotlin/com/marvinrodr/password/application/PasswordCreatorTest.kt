package com.marvinrodr.password.application

import com.marvinrodr.common.domain.Publisher
import com.marvinrodr.password.BaseTest
import com.marvinrodr.password.application.create.PasswordCreator
import com.marvinrodr.password.domain.*
import com.marvinrodr.password.domain.events.PasswordCreated
import com.marvinrodr.password.domain.PasswordRepository
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class PasswordCreatorTest : BaseTest() {
    private lateinit var passwordRepository: PasswordRepository
    private lateinit var publisher: Publisher
    private lateinit var passwordCreator: PasswordCreator

    @BeforeEach
    fun setUp() {
        passwordRepository = mockk(relaxUnitFun = true)
        publisher = mockk(relaxUnitFun = true)
        passwordCreator = PasswordCreator(passwordRepository, publisher)
    }

    @Test
    fun `should create a password successfully`() {
        givenFixedDate(fixedDate)

        passwordCreator.create(id, name, secretKey)

        `then the password should be saved`()
        `then the event should be published`()
    }

    @Test
    fun `should fail with invalid id`() {
        givenFixedDate(fixedDate)

        assertThrows<InvalidPasswordIdException> { passwordCreator.create("Invalid", name, secretKey) }
    }

    @Test
    fun `should fail with invalid name`() {
        givenFixedDate(fixedDate)

        assertThrows<InvalidPasswordNameException> { passwordCreator.create(id, "    ", secretKey) }
    }

    @Test
    fun `should fail with invalid secret key`() {
        givenFixedDate(fixedDate)

        assertThrows<InvalidPasswordSecretKeyException> { passwordCreator.create(id, name, " ") }
    }

    private fun `then the event should be published`() {
        verify { publisher.publish(
                withArg {
                    listOf(event)

                    assertEquals(event, it.first())
                }
            )
        }
    }

    private fun `then the password should be saved`() {
        verify {
            passwordRepository.save(
                withArg {
                    Password(
                        id = PasswordId(UUID.fromString(id)),
                        name = PasswordName(name),
                        secretKey = PasswordSecretKey(secretKey),
                        createdAt = fixedDate,
                        events = listOf(event)
                    )

                    assertEquals(id, it.id.value.toString())
                    assertEquals(name, it.name.value)
                    assertEquals(secretKey, it.secretKey.value)
                    assertEquals(fixedDate, it.createdAt)
                }
            )
        }
    }

    companion object {
        private const val id = "caebae03-3ee9-4aef-b041-21a400fa1bb7"
        private const val name = "Kotlin Hexagonal Architecture Api Password"
        private const val secretKey = "KotlinHexagonalArchitectureApiSecretKey"
        private val fixedDate = LocalDateTime.now()
        private val event = PasswordCreated(
            passwordId = PasswordId(UUID.fromString(id)),
            passwordName  = PasswordName(name),
            secretKey = PasswordSecretKey(secretKey),
            createdAt = fixedDate
        )
    }
}
