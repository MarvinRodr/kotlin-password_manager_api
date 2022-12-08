package com.marvinrodr.password.application.create

import com.marvinrodr.common.Either
import com.marvinrodr.common.Left
import com.marvinrodr.common.Right
import com.marvinrodr.common.domain.Publisher
import com.marvinrodr.password.domain.*
import com.marvinrodr.password.domain.customErrors.*

class PasswordCreator(private val repository: PasswordRepository, private val publisher: Publisher) {
    fun create(id: String, name: String, secretKey: String): Either<PasswordCreateError, Unit> =
        validateId(id).fold(
            ifRight = {
                passwordId ->
                    passwordExist(passwordId).let {
                        exist ->
                            if (!exist) {
                                Right(
                                    Password.create(PasswordId.fromString(id), PasswordName(name), PasswordSecretKey(secretKey)).let {
                                        password ->
                                            repository.save(password).also {
                                                publisher.publish(password.events)
                                            }
                                    }
                                )
                            } else {
                                Left(PasswordCreateIdAlreadyExistError(id))
                            }
                    }
            },
            ifLeft = { Left(it) }
        )

    private fun validateId(id: String): Either<PasswordCreateError, PasswordId> = try {
        Right(PasswordId.fromString(id))
    } catch (exception: InvalidPasswordIdException) {
        Left(PasswordCreateIdNotValidError(exception.id))
    }

    private fun passwordExist(passwordId: PasswordId): Boolean = repository.find(passwordId) is Right
}
