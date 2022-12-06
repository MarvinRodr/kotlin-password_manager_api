package com.marvinrodr.password.application.create

import com.marvinrodr.common.domain.Publisher
import com.marvinrodr.password.domain.*

class PasswordCreator(private val repository: PasswordRepository, private val publisher: Publisher) {
    fun create(id: String, name: String, secretKey: String)  =
        Password.create(PasswordId.fromString(id), PasswordName(name), PasswordSecretKey(secretKey)).let {
            password ->
                repository.save(password).also {
                    publisher.publish(password.events)
                }
        }
}
