package hu.bme.idselector.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

@Serializable(with = CoordinateSerializer::class)
data class Coordinate(
    val x: Float,
    val y: Float
)

object CoordinateSerializer : KSerializer<Coordinate> {
    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor = listSerialDescriptor(Float.serializer().descriptor)

    override fun serialize(encoder: Encoder, value: Coordinate) {
        encoder.encodeStructure(descriptor) {
            encodeFloatElement(descriptor, 0, value.x)
            encodeFloatElement(descriptor, 1, value.y)
        }
    }

    override fun deserialize(decoder: Decoder): Coordinate {
        return decoder.decodeStructure(descriptor) {
            val x = decodeFloatElement(descriptor, 0)
            val y = decodeFloatElement(descriptor, 1)
            Coordinate(x, y)
        }
    }
}
