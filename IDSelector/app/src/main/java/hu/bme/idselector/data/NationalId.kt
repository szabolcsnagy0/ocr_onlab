package hu.bme.idselector.data

import java.util.Date

data class NationalId(
    var id: Int = 0,
    val documentName: String? = null,
    var name: String? = null,
    var sex: Char? = null,
    var nationality: String? = null,
    var dateOfBirth: String? = null,
    var dateOfExpiry: String? = null,
    var documentNr: String? = null,
    var can: String? = null,
    var placeOfBirth: String? = null,
    var nameAtBirth: String? = null,
    var mothersName: String? = null,
    var authority: String? = null,
    var front: String? = null,
    var back: String? = null
)
