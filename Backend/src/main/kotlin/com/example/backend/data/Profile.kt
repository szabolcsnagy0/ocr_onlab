package com.example.backend.data

import jakarta.persistence.*
import org.springframework.data.annotation.Id

@Entity
@Table(name="profiles")
data class Profile(
    @jakarta.persistence.Id @GeneratedValue @Id
    var id: Long = 0,
    val name: String? = null,
    val userId: Long = 0,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "national_id_id")
    val nationalId: NationalId? = null
)
