package com.marvinrodr.password.infrastructure.rest.v1

import com.marvinrodr.password.application.find.PasswordFinder
import com.marvinrodr.password.domain.PasswordResponse
import com.marvinrodr.password.domain.customErrors.PasswordCannotBeFoundError
import com.marvinrodr.password.domain.customErrors.PasswordNotFoundError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class GetFindPasswordByIdController(private val passwordFinder: PasswordFinder) {

    @GetMapping("/password/{id}")
    fun execute(
        @PathVariable id: String
    ): ResponseEntity<PasswordResponse> = passwordFinder.execute(id).fold(
        ifRight = { ResponseEntity.ok().body(it) },
        ifLeft = {
            when (it) {
                is PasswordNotFoundError ->
                    ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build()
                is PasswordCannotBeFoundError ->
                    ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build()
            }
        }
    )
}
