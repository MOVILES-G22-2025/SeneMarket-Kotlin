package com.example.senemarketkotlin.utils
import kotlinx.serialization.*
import com.google.firebase.Timestamp
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.util.Date

object FirebaseTimestampSerializer : KSerializer<Timestamp> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Timestamp) {
        encoder.encodeString(value.toDate().toInstant().toString())
    }

    override fun deserialize(decoder: Decoder): Timestamp {
        val date = Instant.parse(decoder.decodeString())
        return Timestamp(Date.from(date))
    }
}