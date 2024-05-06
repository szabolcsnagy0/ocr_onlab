package hu.bme.idselector.data

import java.util.Date

data class NationalId(
    var id: Int = 0,
    val documentName: String? = null,
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
    var front: String? = null,
    var back: String? = null
)
