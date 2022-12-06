package com.marvinrodr.password.domain

import java.time.LocalDateTime

data class PasswordResponse(val id: String, val name: String, val secretKey: String, val createdAt: LocalDateTime) {
    companion object {
        fun fromPassword(password: Password) = with(password) {
            PasswordResponse(
                id = id.value.toString(),
                name = name.value,
                secretKey = secretKey.value,
                createdAt = createdAt
            )
        }
    }
}