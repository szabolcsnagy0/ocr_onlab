package hu.bme.idselector.data

data class DocumentTemplate(
    val id: Int,
    val name: String,
    val userId: Int,
    val jsonTemplate: String?
)
