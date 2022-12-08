package com.marvinrodr.password.application.update

import com.marvinrodr.common.Either
import com.marvinrodr.common.Left
import com.marvinrodr.common.Right
import com.marvinrodr.password.domain.*
import com.marvinrodr.password.domain.customErrors.PasswordFindError

class PasswordUpdater(private val repository: PasswordRepository) {

    fun execute(id: String, newName: String, newSecretKey: String): Either<PasswordFindError, PasswordResponse> =
        PasswordId.fromString(id).let {
                passwordId ->
                    repository.find(passwordId).fold(
                        ifRight = {
                                    val password = Password.update(it.id, PasswordName(newName), PasswordSecretKey(newSecretKey), it.createdAt)
                                    repository.save(password)

                                    Right(PasswordResponse.fromPassword(password))
                        },
                        ifLeft = { Left(it) }
                    )
        }
}
