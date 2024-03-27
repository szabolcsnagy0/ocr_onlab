package hu.bme.idselector.data

data class Person(
    val name: String?,
    val sex: String?,
    val nationality: String?,
    val dateOfBirth: String?,
    val dateOfExpiry: String?,
    val documentNr: String?,
    val can: String?,
    val placeOfBirth: String?,
    val nameAtBirth: String? = null,
    val mothersName: String?,
    val authority: String?,
)
