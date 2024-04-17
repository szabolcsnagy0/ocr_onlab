package com.example.backend.data

import jakarta.persistence.*
import org.springframework.data.annotation.Id

@Entity
@Table(name = "users")
data class User(
    @Id @jakarta.persistence.Id @GeneratedValue
    var id: Long = 0,
    val email: String? = null,
    @OneToMany(mappedBy = "userId", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val profiles: List<Profile> = emptyList()
)
