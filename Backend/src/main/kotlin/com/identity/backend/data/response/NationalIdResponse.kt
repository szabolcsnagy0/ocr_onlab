package com.identity.backend.data.response

import com.identity.backend.data.entities.NationalId
import java.text.SimpleDateFormat
import java.util.*

data class NationalIdResponse(
    var id: Int = 0,
    val documentName: String? = null,
    val name: String? = null,
    val sex: Char? = null,
    val nationality: String? = null,
    val dateOfBirth: String? = null,
    val dateOfExpiry: String? = null,
    val documentNr: String? = null,
    val can: String? = null,
    val placeOfBirth: String? = null,
    val nameAtBirth: String? = null,
    val mothersName: String? = null,
    val authority: String? = null,
    val profileId: Int = 0
)

fun NationalId.toNationalIdResponse() = NationalIdResponse(
    id = this.id,
    documentName = this.documentName,
    name = this.name,
    documentNr = this.documentNr,
    dateOfBirth = this.dateOfBirth?.formatDate(),
    sex = this.sex,
    can = this.can,
    authority = this.authority,
    profileId = this.profileId,
    dateOfExpiry = this.dateOfExpiry?.formatDate(),
    placeOfBirth = this.placeOfBirth,
    nationality = this.nationality,
    mothersName = this.mothersName,
    nameAtBirth = this.nameAtBirth
)

fun Date.formatDate(): String = SimpleDateFormat("yyyy-MM-dd").format(this)
fun String.convertDate(): Date? = SimpleDateFormat("yyyy-MM-dd").parse(this)
