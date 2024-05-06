package com.identity.backend.data.response

data class NationalIdResponse(
    var id: Int = 0,
    val documentName: String? = null,
    val name: String? = null,
    val sex: Char? = null,
    val nationality: String? = null,
//    val dateOfBirth: Date? = null,
    val dateOfBirth: String? = null,
//    val dateOfExpiry: Date? = null,
    val dateOfExpiry: String? = null,
    val documentNr: String? = null,
    val can: String? = null,
    val placeOfBirth: String? = null,
    val nameAtBirth: String? = null,
    val mothersName: String? = null,
    val authority: String? = null,
    val profileId: Int = 0
)
