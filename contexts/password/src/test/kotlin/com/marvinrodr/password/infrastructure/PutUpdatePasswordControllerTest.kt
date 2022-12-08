package com.marvinrodr.password.infrastructure

import com.marvinrodr.common.Left
import com.marvinrodr.common.Right
import com.marvinrodr.password.application.update.PasswordUpdater
import com.marvinrodr.password.domain.PasswordId
import com.marvinrodr.password.domain.PasswordResponse
import com.marvinrodr.password.domain.customErrors.PasswordNotFoundError
import com.marvinrodr.password.infrastructure.rest.v1.PutUpdatePasswordController
import com.marvinrodr.password.infrastructure.rest.v1.UpdatePasswordRequest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime
import kotlin.test.assertEquals

class PutUpdatePasswordControllerTest {

    private lateinit var passwordUpdater: PasswordUpdater
    private lateinit var controller: PutUpdatePasswordController

    @BeforeEach
    internal fun setUp() {
        passwordUpdater = mockk()
        controller = PutUpdatePasswordController(passwordUpdater)
    }

    @Test
    fun `should return a successful updated response`() {
        every {
            passwordUpdater.execute(newPasswordResponse.id, newPasswordResponse.name, newPasswordResponse.secretKey)
        } returns Right(newPasswordResponse)

        val response = `when a password is requested by id to be updated`()

        assertEquals(ResponseEntity.ok().body(newPasswordResponse), response)
    }

    @Test
    fun `should fail when password is not found`() {
        `given there is no password found`()

        val response = `when a password is requested by id to be updated`()

        `then a not found response is returned`(response)
    }

    private fun `given there is no password found`() {
        every { passwordUpdater.execute(any(),any(),any()) } returns Left(
            PasswordNotFoundError(PasswordId.fromString(passwordId))
        )
    }

    private fun `when a password is requested by id to be updated`(): ResponseEntity<PasswordResponse> =
        controller.execute(
            UpdatePasswordRequest(newPasswordResponse.id, newPasswordResponse.name, newPasswordResponse.secretKey),
            passwordId
        )

    private fun `then a not found response is returned`(actualResponse: ResponseEntity<PasswordResponse>) {
        assertEquals(
            ResponseEntity.status(HttpStatus.NOT_FOUND).build(),
            actualResponse
        )
    }

    companion object {
        private const val passwordId = "e90cadc2-fbf6-49ee-bca4-3fc652ea0134"
        private val newPasswordResponse = PasswordResponse(
            id = passwordId,
            name = "Kotlin API Password",
            secretKey = "My_test_023_secret_key",
            createdAt = LocalDateTime.parse("2022-08-31T09:07:36")
        )
    }
}