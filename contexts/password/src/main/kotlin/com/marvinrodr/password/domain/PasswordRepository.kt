package com.marvinrodr.password.domain

import com.marvinrodr.common.Either
import com.marvinrodr.password.domain.customErrors.PasswordFindError

interface PasswordRepository {
    fun save(password: Password)
    fun find(id: PasswordId): Either<PasswordFindError, Password>
    fun delete(id: PasswordId)
}
