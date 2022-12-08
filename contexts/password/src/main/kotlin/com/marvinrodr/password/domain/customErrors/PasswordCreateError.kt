package com.marvinrodr.password.domain.customErrors

sealed class PasswordCreateError(message: String): Error()

data class PasswordCreateIdAlreadyExistError(val id: String) : PasswordCreateError("The password already exist")
data class PasswordCreateIdNotValidError(val id: String) : PasswordCreateError("The password with id <${id}> is not valid")