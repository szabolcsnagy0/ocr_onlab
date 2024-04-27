package com.identity.backend.data.request

data class AuthenticationRequest(
    val email: String,
    val password: String
)
