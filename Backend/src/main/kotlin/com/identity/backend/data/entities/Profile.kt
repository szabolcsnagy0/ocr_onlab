package com.identity.backend.data.entities

import jakarta.persistence.*
import org.springframework.data.annotation.Id

@Entity
@Table(name="profiles")
data class Profile(
    @jakarta.persistence.Id @Id
    @SequenceGenerator(
        name = "profiles_id_seq",
        sequenceName = "profiles_id_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "profiles_id_seq"
    )
    var id: Int = 0,
    var name: String? = null,
    val userId: Int = 0,
    @OneToMany(mappedBy = "profileId", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val nationalIdList: MutableList<NationalId> = mutableListOf(),
    @OneToMany(mappedBy = "profileId", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val otherIdList: MutableList<OtherId> = mutableListOf()
)
