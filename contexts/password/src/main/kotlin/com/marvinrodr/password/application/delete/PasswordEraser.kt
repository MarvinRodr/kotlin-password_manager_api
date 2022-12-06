package com.marvinrodr.password.application.delete

import com.marvinrodr.common.Either
import com.marvinrodr.common.Left
import com.marvinrodr.common.Right
import com.marvinrodr.common.domain.Publisher
import com.marvinrodr.password.domain.Password
import com.marvinrodr.password.domain.PasswordId
import com.marvinrodr.password.domain.PasswordRepository
import com.marvinrodr.password.domain.customErrors.PasswordFindError

class PasswordEraser(private val passwordRepository: PasswordRepository, private val publisher: Publisher) {

    fun execute(passwordId: String): Either<PasswordFindError, Unit> =
        PasswordId.fromString(passwordId).let {
                id ->
                    passwordRepository.find(id).fold(
                        ifRight = { password ->
                            Right(
                                Password.delete(password.id, password.name, password.secretKey, password.createdAt).let {
                                    passwordRepository.delete(it.id).also { _ ->
                                        publisher.publish(it.events)
                                    }
                                }
                            )
                        },
                        ifLeft = { Left(it) }
                    )
        }
}