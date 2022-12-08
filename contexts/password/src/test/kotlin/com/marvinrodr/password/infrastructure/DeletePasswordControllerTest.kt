package com.marvinrodr.password.infrastructure

import com.marvinrodr.common.Left
import com.marvinrodr.common.Right
import com.marvinrodr.password.application.delete.PasswordEraser
import com.marvinrodr.password.domain.PasswordId
import com.marvinrodr.password.domain.customErrors.PasswordNotFoundError
import com.marvinrodr.password.infrastructure.rest.v1.DeletePasswordController
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals

class DeletePasswordControllerTest {

    private lateinit var passwordEraser: PasswordEraser
    private lateinit var controller: DeletePasswordController

    @BeforeEach
    internal fun setUp() {
        passwordEraser = mockk()
        controller = DeletePasswordController(passwordEraser)
    }

    @Test
    fun `should return a successful deleted response`() {
        every { passwordEraser.execute(passwordId) } returns Right(Unit)

        val response = controller.execute(passwordId)

        assertEquals(ResponseEntity.ok().build(), response)
    }

    @Test
    fun `should fail when password is not found`() {
        `given there is no password found`()

        val response = `when a password is requested by id to be deleted`()

        `then a not found response is returned`(response)
    }

    private fun `given there is no password found`() {
        every { passwordEraser.execute(any()) } returns Left(
            PasswordNotFoundError(PasswordId.fromString(passwordId))
        )
    }

    private fun `when a password is requested by id to be deleted`() = controller.execute(passwordId)

    private fun `then a not found response is returned`(actualResponse: ResponseEntity<Unit>) {
        assertEquals(
            ResponseEntity.status(HttpStatus.NOT_FOUND).build(),
            actualResponse
        )
    }

    companion object {
        private const val passwordId = "e90cadc2-fbf6-49ee-bca4-3fc652ea0134"
    }
}