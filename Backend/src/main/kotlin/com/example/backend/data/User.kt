package com.example.backend.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import org.springframework.data.annotation.Id

@Entity
@Table(name = "users")
data class User(
    @Id @jakarta.persistence.Id @GeneratedValue
    var id: Long = 0,
    val email: String? = null
)
