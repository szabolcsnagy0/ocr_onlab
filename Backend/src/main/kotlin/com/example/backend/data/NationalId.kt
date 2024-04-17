package com.example.backend.data

import jakarta.persistence.*
import org.springframework.data.annotation.Id
import java.util.*

@Entity
@Table(name = "national_id")
data class NationalId(
    @jakarta.persistence.Id @GeneratedValue @Id
    var id: Long = 0,
    val name: String? = null,
    val sex: Char? = null,
    val nationality: String? = null,
    val dateOfBirth: Date? = null,
    val dateOfExpiry: Date? = null,
    val documentNr: String? = null,
    val can: String? = null,
    val placeOfBirth: String? = null,
    val nameAtBirth: String? = null,
    val mothersName: String? = null,
    val authority: String? = null,
)
