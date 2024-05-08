package com.identity.backend.data.entities

import com.identity.backend.data.response.NationalIdResponse
import jakarta.persistence.*
import org.springframework.data.annotation.Id
import java.util.Date

@Entity
@Table(name = "national_id")
data class NationalId(
    @jakarta.persistence.Id @Id
    @SequenceGenerator(
        name = "national_id_id_seq",
        sequenceName = "national_id_id_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "national_id_id_seq"
    )
    var id: Int = 0,
    var documentName: String? = null,
    val name: String? = null,
    val sex: Char? = null,
    val nationality: String? = null,
    val dateOfBirth: Date? = null,
//    val dateOfBirth: String? = null,
    val dateOfExpiry: Date? = null,
//    val dateOfExpiry: String? = null,
    val documentNr: String? = null,
    val can: String? = null,
    val placeOfBirth: String? = null,
    val nameAtBirth: String? = null,
    val mothersName: String? = null,
    val authority: String? = null,
    var front: ByteArray? = null,
    var back: ByteArray? = null,
    val profileId: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NationalId
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (documentNr?.hashCode() ?: 0)
        return result
    }
}
