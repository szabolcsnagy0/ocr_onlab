package com.identity.backend.services

import com.identity.backend.config.JwtProperties
import com.identity.backend.data.entities.User
import com.identity.backend.data.request.AuthenticationRequest
import com.identity.backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService (
    private val authenticationManager: AuthenticationManager,
    private val customUserDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val encoder: PasswordEncoder
) {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun authentication(authRequest: AuthenticationRequest): String {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.email,
                authRequest.password
            )
        )

        val user = customUserDetailsService.loadUserByUsername(authRequest.email)
        val accessToken = tokenService.generate(
            userDetails = user,
            expirationDate = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
        )

        return accessToken
    }

    fun registration(registrationRequest: AuthenticationRequest) {
        // Check whether the user already exists
        userRepository.findByEmail(registrationRequest.email)?.let {
            throw IllegalArgumentException("The user already exists.");
        }

        val hashedPassword = encoder.encode(registrationRequest.password)
        val newUser = User(email = registrationRequest.email, password = hashedPassword)

        // Save the user to the database
        userRepository.save(newUser)
    }

    fun getEmail(): String? {
        return try {
//            println((SecurityContextHolder.getContext().authentication.principal as UserDetails).username)
            (SecurityContextHolder.getContext().authentication.principal as UserDetails).username
        } catch(_: Exception) {
            null
        }
    }
}
