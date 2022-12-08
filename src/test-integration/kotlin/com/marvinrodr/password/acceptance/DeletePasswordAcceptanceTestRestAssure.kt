package com.marvinrodr.password.acceptance

import com.marvinrodr.common.password.PasswordMother
import com.marvinrodr.shared.acceptance.BaseAcceptanceTest
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class DeletePasswordAcceptanceTestRestAssure: BaseAcceptanceTest() {

    @Test
    @Sql("classpath:db/fixtures/password/find/add-password-data.sql")
    fun `should delete password successfully`() {
        When {
            delete("$version/password/${password.id.value}")
        } Then {
            statusCode(HttpStatus.OK.value())
        }
    }

    @Test
    @Sql("classpath:db/fixtures/password/find/add-password-data.sql")
    fun `should not delete password with status not found`() {
        When {
            delete("$version/password/${notExistingPasswordId}")
        } Then {
            statusCode(HttpStatus.NOT_FOUND.value())
        }
    }

    companion object {
        private const val version = "/api/v1"
        private val now = LocalDateTime.parse("2022-08-31T09:07:36")
        private val password = PasswordMother.sample(
            id = "7ab75530-5da7-4b4a-b083-a779dd6c759e",
            name = "Saved password name",
            secretKey = "MySuperSecretKey",
            createdAt = now
        )
        private const val notExistingPasswordId = "aabc1f33-96b4-4402-b47e-467ed2bb0233"
    }
}