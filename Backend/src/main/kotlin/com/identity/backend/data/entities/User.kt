package com.identity.backend.data.entities

import jakarta.persistence.*
import org.springframework.data.annotation.Id

@Entity
@Table(name = "users")
data class User(
    @Id @jakarta.persistence.Id
    @SequenceGenerator(
        name = "users_id_seq",
        sequenceName = "users_id_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "users_id_seq"
    )
    val id: Int = 0,
    val email: String? = null,
    val password: String? = null,
    @OneToMany(mappedBy = "userId", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val profiles: MutableList<Profile> = mutableListOf(),
    @OneToMany(mappedBy = "userId", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val documentTemplates: MutableList<DocumentTemplate> = mutableListOf()
)
