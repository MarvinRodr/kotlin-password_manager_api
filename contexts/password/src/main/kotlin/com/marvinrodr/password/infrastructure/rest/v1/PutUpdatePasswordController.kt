package com.marvinrodr.password.infrastructure.rest.v1


import com.marvinrodr.password.application.update.PasswordUpdater
import com.marvinrodr.password.domain.*
import com.marvinrodr.password.domain.customErrors.PasswordCannotBeFoundError
import com.marvinrodr.password.domain.customErrors.PasswordIdNotValidError
import com.marvinrodr.password.domain.customErrors.PasswordNotFoundError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class PutUpdatePasswordController(private val passwordUpdater: PasswordUpdater) {

    @PutMapping("/password/{id}")
    fun execute(
        @RequestBody request: UpdatePasswordRequest,
        @PathVariable id: String
    ): ResponseEntity<PasswordResponse> =
        passwordUpdater.execute(request.id, request.name, request.secretKey).fold(
            ifRight = { ResponseEntity.ok().body(it) },
            ifLeft = {
                when (it) {
                    is PasswordNotFoundError ->
                        ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .build()
                    is PasswordIdNotValidError ->
                        ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
                    is PasswordCannotBeFoundError ->
                        ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build()
                }
            }
        )
}

data class UpdatePasswordRequest(
    val id: String,
    val name: String,
    val secretKey: String
)
