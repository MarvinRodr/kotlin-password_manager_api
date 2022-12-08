package com.marvinrodr.password.infrastructure.rest.v1

import com.marvinrodr.password.application.delete.PasswordEraser
import com.marvinrodr.password.domain.*
import com.marvinrodr.password.domain.customErrors.PasswordCannotBeFoundError
import com.marvinrodr.password.domain.customErrors.PasswordIdNotValidError
import com.marvinrodr.password.domain.customErrors.PasswordNotFoundError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class DeletePasswordController(private val passwordEraser: PasswordEraser) {

    @DeleteMapping("/password/{id}")
    fun execute(
        @PathVariable id: String
    ): ResponseEntity<Unit> = passwordEraser.execute(id).fold(
        ifRight = { ResponseEntity.ok().build() },
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
