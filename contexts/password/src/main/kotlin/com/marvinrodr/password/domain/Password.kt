package com.marvinrodr.password.domain

import com.marvinrodr.common.domain.DomainEvent
import com.marvinrodr.password.domain.events.PasswordCreated
import java.time.LocalDateTime
import java.util.UUID

data class PasswordId(val value: UUID){
    companion object {
        fun fromString(id: String) = try {
            PasswordId(UUID.fromString(id))
        } catch (exception: Exception) {
            throw InvalidPasswordIdException(id, exception)
        }
    }
}
data class PasswordName(val value: String) {
    init {
        validate()
    }

    private fun validate() {
        if (value.isEmpty() || value.isBlank()) {
            throw InvalidPasswordNameException(value)
        }
    }
}

data class PasswordSecretKey(val value: String) {
    init {
        validate()
    }

    private fun validate() {
        if (value.isEmpty() || value.isBlank()) {
            throw InvalidPasswordSecretKeyException(value)
        }
    }
}

data class Password(
    val id: PasswordId,
    val name: PasswordName,
    val secretKey: PasswordSecretKey,
    val createdAt: LocalDateTime,
    val events: List<DomainEvent>
) {
    companion object {
        fun create(
            id: PasswordId,
            name: PasswordName,
            secretKey: PasswordSecretKey,
        ) = LocalDateTime.now().let { createdAt ->
            Password(
                id,
                name,
                secretKey,
                createdAt,
                listOf(PasswordCreated(id, name, secretKey, createdAt))
            )
        }
    }
}
