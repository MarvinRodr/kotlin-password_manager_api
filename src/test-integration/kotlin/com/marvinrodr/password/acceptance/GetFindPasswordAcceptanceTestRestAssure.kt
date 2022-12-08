package com.marvinrodr.password.acceptance

import com.marvinrodr.common.password.PasswordMother
import com.marvinrodr.password.infrastructure.persistence.PostgrePasswordRepository
import com.marvinrodr.shared.acceptance.BaseAcceptanceTest
import com.marvinrodr.shared.utils.isEqualToJson
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class GetFindPasswordAcceptanceTestRestAssure: BaseAcceptanceTest() {

    @Autowired
    private lateinit var passwordRepository: PostgrePasswordRepository

    @Test
    @Sql("classpath:db/fixtures/password/find/add-password-data.sql")
    fun `should find password successfully`() {
        When {
            get("$version/password/${password.id.value}")
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            body().asString().isEqualToJson(expectedPasswordResponse)
        }
    }

    @Test
    fun `should find password successfully with password creation`() {
        Given {
            `an existin password`()
            contentType(ContentType.JSON)
            body("")
        } When {
            get("$version/password/${password.id.value}")
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            body().asString().isEqualToJson(expectedPasswordResponse)
        }
    }

    private fun `an existin password`() {
        passwordRepository.save(password)
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
        private val expectedPasswordResponse = """
                {
                    "id": "${password.id.value}",
                    "name": "${password.name.value}",
                    "secretKey": "${password.secretKey.value}",
                    "createdAt": "$now"
                }
            """.trimIndent()
    }
}