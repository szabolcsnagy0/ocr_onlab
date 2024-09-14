package hu.bme.idselector.data

data class Document(
    val id: Int,
    val documentName: String? = null,
    val front: String? = null,
    val back: String? = null,
    val fieldsList: List<DocumentField>
)
