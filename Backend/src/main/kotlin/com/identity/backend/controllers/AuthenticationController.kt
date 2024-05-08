package com.identity.backend.controllers

import com.identity.backend.data.request.AuthenticationRequest
import com.identity.backend.services.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService,
) {
    @CrossOrigin
    @PostMapping("login")
    fun authenticate(@RequestBody authRequest: AuthenticationRequest): String =
        authenticationService.authentication(authRequest)

    @CrossOrigin
    @PostMapping("register")
    fun registerUser(@RequestBody registrationRequest: AuthenticationRequest): ResponseEntity<String?> {
        try {
            authenticationService.registration(registrationRequest)
            return ResponseEntity.ok(null)
        }
        catch (exception: Exception) {
            return ResponseEntity.badRequest().body(exception.message)
        }
    }

    @CrossOrigin
    @GetMapping("token")
    fun checkToken(): ResponseEntity<*> {
        return ResponseEntity.ok(null)
    }
}