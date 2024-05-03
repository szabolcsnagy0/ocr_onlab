package hu.bme.idselector.data

data class User(
    var id: Int = 0,
    val email: String? = null,
    val profiles: List<Profile> = emptyList()
)
