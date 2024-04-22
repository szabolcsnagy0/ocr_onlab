package hu.bme.idselector.data

data class Profile(
    val id: Long = 0,
    val name: String? = null,
    val userId: Long = 0,
    val nationalId: NationalId? = null
)
