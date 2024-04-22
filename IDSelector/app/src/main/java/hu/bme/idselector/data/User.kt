package hu.bme.idselector.data

data class User(
    var id: Long = 0,
    val email: String? = null,
    val profiles: List<Profile> = emptyList()
)
