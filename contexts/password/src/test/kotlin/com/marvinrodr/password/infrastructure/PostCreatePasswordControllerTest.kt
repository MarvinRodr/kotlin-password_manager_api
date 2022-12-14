package com.marvinrodr.password.infrastructure

import com.marvinrodr.common.Right
import com.marvinrodr.password.application.create.PasswordCreator
import com.marvinrodr.password.domain.InvalidPasswordIdException
import com.marvinrodr.password.domain.InvalidPasswordNameException
import com.marvinrodr.password.infrastructure.rest.v1.CreatePasswordRequest
import com.marvinrodr.password.infrastructure.rest.v1.PostCreatePasswordController
import io.mockk.every
import io.mockk.mockk
import java.net.URI
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class PostCreatePasswordControllerTest {

    private lateinit var passwordCreator: PasswordCreator
    private lateinit var controller: PostCreatePasswordController

    @BeforeEach
    fun setUp() {
        passwordCreator = mockk()
        controller = PostCreatePasswordController(passwordCreator)
    }

    @Test
    fun `should return a successful response`() {
        every { passwordCreator.create(any(), any(), any()) } returns Right(Unit)

        val passwordId = "03ef970b-719d-49c5-8d80-7dc762fe4be6"
        val response = controller.execute(CreatePasswordRequest(passwordId, "Test", "my_successfully_secret_key"))

        assertEquals(ResponseEntity.created(URI.create("/password/$passwordId")).build(), response)
    }

    @Test
    fun `should fail when id is not valid`() {
        every { passwordCreator.create(any(), any(), any()) } throws InvalidPasswordIdException("1", null)

        val response = controller.execute(CreatePasswordRequest("1", "Test", "invalid_id_secret_key"))

        assertEquals(ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("The password id is not valid"), response)
    }

    @Test
    fun `should fail when name is not valid`() {
        every { passwordCreator.create(any(), any(), any()) } throws InvalidPasswordNameException("Invalid")

        val response = controller.execute(CreatePasswordRequest("03ef970b-719d-49c5-8d80-7dc762fe4be6", "Invalid", "Invalid_secret_key"))

        assertEquals(ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("The password name is not valid"), response)
    }

    @Test
    fun `should fail when there is an uncontrolled exception`() {
        every { passwordCreator.create(any(), any(), any()) } throws Throwable()

        val response = controller.execute(CreatePasswordRequest("03ef970b-719d-49c5-8d80-7dc762fe4be6", "Test", "uncontrolled_secret_key"))

        assertEquals(ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build(), response)
    }
}
