package com.marvinrodr.password.application

import com.marvinrodr.common.Either
import com.marvinrodr.common.Left
import com.marvinrodr.common.Right
import com.marvinrodr.common.password.PasswordMother
import com.marvinrodr.password.BaseTest
import com.marvinrodr.password.application.find.PasswordFinder
import com.marvinrodr.password.domain.*
import com.marvinrodr.password.domain.customErrors.PasswordFindError
import com.marvinrodr.password.domain.customErrors.PasswordNotFoundError
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PasswordFinderTest : BaseTest() {

    private lateinit var passwordRepository: PasswordRepository
    private lateinit var passwordFinder: PasswordFinder

    @BeforeEach
    internal fun setUp() {
        passwordRepository = mockk(relaxUnitFun = true)
        passwordFinder = PasswordFinder(passwordRepository)
    }

    @Test
    fun `should find an existing password`() {
        `given an saved password`()

        val actualResult = `when the finder is executed`()

        `then the found password is equals to expected`(actualResult)
    }

    @Test
    fun `should throw an exception when password is not found`() {
        `given no password is saved`()

        val actualResult = `when the finder is executed`()

        `then the result is a failure with no found exception`(actualResult)
    }

    private fun `given no password is saved`() {
        every { passwordRepository.find(passwordId) } returns Left(
            PasswordNotFoundError(passwordId)
        )
    }

    private fun `then the found password is equals to expected`(actualResult: Either<PasswordFindError, PasswordResponse> ) {
        val expected =  Right(
            PasswordResponse(
                id = id,
                name = passwordName,
                secretKey = secretKey,
                createdAt = passwordCreatedAt
            )
        )

        assertEquals(expected, actualResult)
    }

    private fun `then the result is a failure with no found exception`(actualResult: Either<PasswordFindError, PasswordResponse>) {
        val expected =  Left<PasswordFindError>(
            PasswordNotFoundError(passwordId)
        )

        assertEquals(expected, actualResult)
    }

    private fun `when the finder is executed`(): Either<PasswordFindError, PasswordResponse> {
        return passwordFinder.execute(id)
    }

    private fun `given an saved password`() {

        val password = PasswordMother.sample(
            id = id,
            name = passwordName,
            secretKey = secretKey,
            createdAt = passwordCreatedAt
        )
        every { passwordRepository.find(password.id) } returns Right(password)
    }

    companion object {
        private const val id = "7ab75530-5da7-4b4a-b083-a779dd6c759e"
        private val passwordId = PasswordId.fromString(id)
        private const val passwordName = "Password Finder Test Name"
        private const val secretKey = "KotlinHexagonalArchitectureApiSecretKey"
        private val passwordCreatedAt = LocalDateTime.parse("2022-08-31T09:00:00")
    }
}
