package hu.bme.idselector.data

data class DocumentTemplate(
    val id: Int? = null,
    val name: String,
    val userId: Int? = null,
    val jsonTemplate: String?
)
