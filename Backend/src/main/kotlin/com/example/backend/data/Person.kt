package com.example.backend.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import org.springframework.data.annotation.Id

@Entity
data class Person(
    @jakarta.persistence.Id @GeneratedValue @Id
    var id: Long = 0,
    val name: String? = null,
    val userId: Long = 0,
)
