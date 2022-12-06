package com.marvinrodr.password.domain.events

import com.marvinrodr.common.domain.DomainEvent
import com.marvinrodr.password.domain.PasswordId
import com.marvinrodr.password.domain.PasswordName
import com.marvinrodr.password.domain.PasswordSecretKey
import java.time.LocalDateTime

class PasswordDeleted(
    passwordId: PasswordId,
    passwordName: PasswordName,
    secretKey: PasswordSecretKey,
    createdAt: LocalDateTime
) : DomainEvent(
    type = "PasswordDeleted",
    payload = """
    {
        "id": ${passwordId.value},
        "name": ${passwordName.value},
        "secret_key": ${secretKey.value},
        "created_at": $createdAt
    }
    """.trimIndent()
) {
    // Override functions
    override fun equals(other: Any?): Boolean = (other is PasswordDeleted) && payload == other.payload
    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + payload.hashCode()
        return result
    }
}
