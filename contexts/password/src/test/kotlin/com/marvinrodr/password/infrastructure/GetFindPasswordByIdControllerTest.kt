package com.marvinrodr.password.infrastructure

import com.marvinrodr.common.Left
import com.marvinrodr.common.Right
import com.marvinrodr.password.application.find.PasswordFinder
import com.marvinrodr.password.domain.PasswordResponse
import com.marvinrodr.password.domain.PasswordId
import com.marvinrodr.password.domain.customErrors.PasswordNotFoundError
import com.marvinrodr.password.infrastructure.rest.v1.GetFindPasswordByIdController
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class GetFindPasswordByIdControllerTest {

    private lateinit var passwordFinder: PasswordFinder
    private lateinit var controller: GetFindPasswordByIdController

    @BeforeEach
    internal fun setUp() {
        passwordFinder = mockk()
        controller = GetFindPasswordByIdController(passwordFinder)
    }

    @Test
    fun `should return the password response`() {
        `given a password response`()

        val response = `when a password is requested by id`()

        `then a successful response is returned`(response)
    }

    private fun `then a successful response is returned`(actualResponse: ResponseEntity<PasswordResponse>) {
        assertEquals(ResponseEntity(password, HttpStatus.OK), actualResponse)
    }

    private fun `when a password is requested by id`() = controller.execute(passwordId)

    private fun `given a password response`() {
        every { passwordFinder.execute(any()) } returns Right(password)
    }

    @Test
    fun `should fail when password is not found`() {
        `given there is no password found`()

        val response = `when a password is requested by id`()

        `then a not found response is returned`(response)
    }

    private fun `then a not found response is returned`(actualResponse: ResponseEntity<PasswordResponse>) {
        assertEquals(
            ResponseEntity.status(HttpStatus.NOT_FOUND).build(),
            actualResponse
        )
    }

    private fun `given there is no password found`() {
        every { passwordFinder.execute(any()) } returns Left(
            PasswordNotFoundError(PasswordId.fromString(passwordId))
        )
    }

    companion object {
        private const val passwordId = "e90cadc2-fbf6-49ee-bca4-3fc652ea0134"
        private val password = PasswordResponse(
            id = passwordId,
            name = "Kotlin API Password",
            secretKey = "My_test_023_secret_key",
            createdAt = LocalDateTime.parse("2022-08-31T09:07:36")
        )
    }
}
