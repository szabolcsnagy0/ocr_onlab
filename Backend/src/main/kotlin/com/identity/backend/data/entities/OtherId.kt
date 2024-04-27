package com.identity.backend.data.entities

import jakarta.persistence.*
import org.springframework.data.annotation.Id

@Entity
@Table(name = "other_id")
data class OtherId (
    @jakarta.persistence.Id @Id
    @SequenceGenerator(
        name = "other_id_id_seq",
        sequenceName = "other_id_id_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "other_id_id_seq"
    )
    val id: Int = 0,
    var front: ByteArray? = null,
    val back: ByteArray? = null,
    val profileId: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OtherId

        if (id != other.id) return false
        if (!front.contentEquals(other.front)) return false
        if (!back.contentEquals(other.back)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + front.contentHashCode()
        result = 31 * result + back.contentHashCode()
        return result
    }
}