package com.marvinrodr.password.domain.customErrors

import com.marvinrodr.password.domain.PasswordId

sealed class PasswordFindError(message: String): Error()

data class PasswordNotFoundError(val id: PasswordId) : PasswordFindError("The password with id <${id.value}> was not found")
data class PasswordCannotBeFoundError(val id: PasswordId) : PasswordFindError("Something went wrong trying to find a password with id <${id.value}>")