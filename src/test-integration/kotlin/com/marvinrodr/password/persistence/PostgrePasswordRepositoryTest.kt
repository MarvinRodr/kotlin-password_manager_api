package com.marvinrodr.password.persistence

import com.marvinrodr.common.Left
import com.marvinrodr.common.Right
import com.marvinrodr.common.password.PasswordMother
import com.marvinrodr.password.domain.*
import com.marvinrodr.password.domain.customErrors.PasswordNotFoundError
import com.marvinrodr.password.infrastructure.persistence.PostgrePasswordRepository
import com.marvinrodr.shared.persistence.BaseIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

class PostgrePasswordRepositoryTest: BaseIntegrationTest() {

    @Autowired
    private lateinit var repository: PostgrePasswordRepository

    @Test
    fun `should save a password`() {
        val passwordId = "13590efb-c181-4c5f-9f95-b768abde13e2"
        val passwordName = "name"
        val passwordSecretKey = "secretKey"
        val passwordToSave = PasswordMother.sample(
            id = passwordId,
            name = passwordName,
            secretKey = passwordSecretKey
        )
        repository.save(passwordToSave)

        val passwordFromDb = repository.find(PasswordId.fromString(passwordId))

        assertEquals(Right(passwordToSave), passwordFromDb)
    }

    @Test
    fun `should fail when password is not found`() {
        val passwordId = "13590efb-c181-4c5f-9f95-b768abde13e2"

        val passwordFromDb = repository.find(PasswordId.fromString(passwordId))

        assertEquals(Left(PasswordNotFoundError(PasswordId.fromString(passwordId))), passwordFromDb)
    }
}