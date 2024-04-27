package com.identity.backend.data.request

import java.util.*

data class NationalIdRequest(
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
    val front: String? = null,
    val back: String? = null
)