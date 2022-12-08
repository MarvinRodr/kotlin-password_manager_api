package com.marvinrodr.password.application.find

import com.marvinrodr.common.Either
import com.marvinrodr.common.Left
import com.marvinrodr.common.Right
import com.marvinrodr.password.domain.*
import com.marvinrodr.password.domain.customErrors.PasswordFindError
import com.marvinrodr.password.domain.customErrors.PasswordIdNotValidError

class PasswordFinder(private val passwordRepository: PasswordRepository) {
    fun execute(id: String): Either<PasswordFindError, PasswordResponse> =
        validateId(id).fold(
            ifRight = { passwordId ->
                passwordRepository.find(passwordId).fold(
                    ifRight = { Right(PasswordResponse.fromPassword(it)) },
                    ifLeft = { Left(it) }
                )
            },
            ifLeft = { Left(it) }
        )


    private fun validateId(id: String): Either<PasswordFindError, PasswordId> = try {
        Right(PasswordId.fromString(id))
    } catch (exception: InvalidPasswordIdException) {
        Left(PasswordIdNotValidError(exception.id))
    }
}
