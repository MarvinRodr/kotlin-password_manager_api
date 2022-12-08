package com.marvinrodr.password.infrastructure.rest.v1

import com.marvinrodr.password.application.create.PasswordCreator
import com.marvinrodr.password.domain.*
import com.marvinrodr.password.domain.customErrors.PasswordCreateIdAlreadyExistError
import com.marvinrodr.password.domain.customErrors.PasswordCreateIdNotValidError
import java.net.URI
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class PostCreatePasswordController(private val passwordCreator: PasswordCreator) {

    @PostMapping("/password")
    fun execute(
        @RequestBody request: CreatePasswordRequest
    ): ResponseEntity<String> {
        return try {
            passwordCreator.create(request.id, request.name, request.secretKey).fold(
                ifRight = { ResponseEntity.created(URI.create("/password/${request.id}")).build() },
                ifLeft = {
                    when (it) {
                        is PasswordCreateIdAlreadyExistError ->
                            ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("The password already exist")
                        is PasswordCreateIdNotValidError ->
                            ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("The password id is not valid")
                    }
                }
            )

        } catch (exception: InvalidArgumentPasswordException) {
            when (exception) {
                is InvalidPasswordIdException -> ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The password id is not valid")

                is InvalidPasswordNameException -> ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The password name is not valid")

                is InvalidPasswordSecretKeyException -> ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The password secret key is not valid")
            }
        } catch (exception: Throwable) {
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build()
        }
    }
}

data class CreatePasswordRequest(
    val id: String,
    val name: String,
    val secretKey: String
)
