package hu.bme.idselector.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TemplateField(
    val text: String,
    @SerialName("place") val textCoords: List<Coordinate>,
    @SerialName("value") val valueCoords: List<Coordinate>
)
