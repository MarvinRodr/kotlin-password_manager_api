package com.marvinrodr.password.application.find

import com.marvinrodr.common.Either
import com.marvinrodr.common.Left
import com.marvinrodr.common.Right
import com.marvinrodr.password.domain.*
import com.marvinrodr.password.domain.customErrors.PasswordFindError

class PasswordFinder(private val passwordRepository: PasswordRepository) {
    fun execute(passwordId: String): Either<PasswordFindError, PasswordResponse> =
        PasswordId.fromString(passwordId).let {
            id ->
                passwordRepository.find(id).fold(
                    ifRight = { Right(PasswordResponse.fromPassword(it)) },
                    ifLeft = { Left(it) }
                )
        }
}
