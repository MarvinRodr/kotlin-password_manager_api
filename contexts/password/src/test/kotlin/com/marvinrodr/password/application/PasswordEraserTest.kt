package com.marvinrodr.password.application

import com.marvinrodr.common.Either
import com.marvinrodr.common.Left
import com.marvinrodr.common.Right
import com.marvinrodr.common.domain.Publisher
import com.marvinrodr.common.password.PasswordMother
import com.marvinrodr.password.BaseTest
import com.marvinrodr.password.application.delete.PasswordEraser
import com.marvinrodr.password.domain.*
import com.marvinrodr.password.domain.customErrors.PasswordFindError
import com.marvinrodr.password.domain.customErrors.PasswordNotFoundError
import com.marvinrodr.password.domain.events.PasswordDeleted
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

class PasswordEraserTest : BaseTest() {

    private lateinit var passwordRepository: PasswordRepository
    private lateinit var publisher: Publisher
    private lateinit var passwordEraser: PasswordEraser

    @BeforeEach
    internal fun setUp() {
        passwordRepository = mockk(relaxUnitFun = true)
        publisher = mockk(relaxUnitFun = true)
        passwordEraser = PasswordEraser(passwordRepository, publisher)
    }
    @Test
    fun `should delete an existing password`() {
        `given an saved password`()

        `when the eraser is executed`()

        `then the password should be deleted`()
        `then the event should be published`()
    }

    @Test
    fun `should throw an exception when password is not found`() {
        `given no password is saved`()

        val result = `when the eraser is executed`()

        `then the result is a failure with no found exception`(result)
    }


    private fun `then the event should be published`() {
        verify {
            publisher.publish(
                withArg {
                    listOf(event)
                    assertEquals(event, it.first())
                }
            )
        }
    }

    private fun `then the password should be deleted`() {
        verify {
            passwordRepository.delete(PasswordId(UUID.fromString(id)))
        }
    }

    private fun `then the result is a failure with no found exception`(actualResult: Either<PasswordFindError, Unit>) {
        val expected =  Left<PasswordFindError>(
            PasswordNotFoundError(PasswordId(UUID.fromString(id)))
        )

        assertEquals(expected, actualResult)
    }

    private fun `when the eraser is executed`(): Either<PasswordFindError, Unit> {
        return passwordEraser.execute(id)
    }

    private fun `given an saved password`() {

        val password = PasswordMother.sample(
            id = id,
            name = name,
            secretKey = secretKey,
            createdAt = fixedDate
        )
        every { passwordRepository.find(password.id) } returns Right(password)
    }

    private fun `given no password is saved`() {
        every {
            passwordRepository.find(PasswordId(UUID.fromString(id)))
        } returns Left(
            PasswordNotFoundError(PasswordId(UUID.fromString(id)))
        )
    }

    companion object {
        private const val id = "caebae03-3ee9-4aef-b041-21a400fa1bb7"
        private const val name = "Kotlin Hexagonal Architecture Api Password"
        private const val secretKey = "KotlinHexagonalArchitectureApiSecretKey"
        private val fixedDate = LocalDateTime.now()
        private val event = PasswordDeleted(
            passwordId = PasswordId(UUID.fromString(id)),
            passwordName  = PasswordName(name),
            secretKey = PasswordSecretKey(secretKey),
            createdAt = fixedDate
        )
    }
}