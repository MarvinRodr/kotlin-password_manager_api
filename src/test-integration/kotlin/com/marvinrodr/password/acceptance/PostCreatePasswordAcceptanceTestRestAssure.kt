package com.marvinrodr.password.acceptance

import com.marvinrodr.shared.acceptance.BaseAcceptanceTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class PostCreatePasswordAcceptanceTestRestAssure: BaseAcceptanceTest() {

    @Test
    fun `should create a password successfully`() {
        Given {
            contentType(ContentType.JSON)
            body(
                """
               {
                    "id": "97fa5af4-bd81-45d5-974f-d5a3970af252",
                    "name": "Test Acceptance",
                    "secretKey": "TestAcceptanceSecretKey"
               }
                """
            )
        } When  {
            post("/password")
        } Then  {
            statusCode(HttpStatus.CREATED.value())
        }
    }
}