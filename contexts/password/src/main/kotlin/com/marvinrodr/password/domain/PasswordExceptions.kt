package com.marvinrodr.password.domain

sealed class InvalidArgumentPasswordException(override val message: String, override val cause: Throwable? = null) : IllegalArgumentException(message, cause)

data class InvalidPasswordIdException(val id: String, override val cause: Throwable?) : InvalidArgumentPasswordException("The id <$id> is not a valid password id", cause)
data class InvalidPasswordSecretKeyException(val secretKey: String) : InvalidArgumentPasswordException("The secret key <$secretKey> is not a valid password secret key")
data class InvalidPasswordNameException(val name: String) : InvalidArgumentPasswordException("The name <$name> is not a valid password name")
