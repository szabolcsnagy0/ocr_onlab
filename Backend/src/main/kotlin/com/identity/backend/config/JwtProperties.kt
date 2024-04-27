package com.identity.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties
@ConstructorBinding constructor(
    val key: String,
    val accessTokenExpiration: Long,
    val refreshTokenExpiration: Long
)
