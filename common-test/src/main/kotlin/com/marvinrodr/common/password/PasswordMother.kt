package com.marvinrodr.common.password

import com.marvinrodr.password.domain.Password
import com.marvinrodr.password.domain.PasswordId
import com.marvinrodr.password.domain.PasswordName
import com.marvinrodr.password.domain.PasswordSecretKey
import java.time.LocalDateTime

object PasswordMother {

    fun sample(
        id: String = "7ab75530-5da7-4b4a-b083-a779dd6c759e",
        name: String = "Password Mother Name",
        secretKey: String = "KotlinHexagonalArchitectureApiSecretKey",
        createdAt: LocalDateTime = LocalDateTime.parse("2022-08-31T09:00:00")
    ) = Password(
        id = PasswordId.fromString(id),
        name = PasswordName(name),
        secretKey = PasswordSecretKey(secretKey),
        createdAt = createdAt,
        events = listOf()
    )
}
