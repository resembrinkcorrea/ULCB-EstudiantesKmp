package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.RequisitoTramiteB
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.RequisitoTramiteD

@Serializable
sealed class RequisitosTramiteSealed {
    @Serializable
    data class RequisitoTramiteTempA(val requisitos: List<RequisitoTramiteModoCheck>) : RequisitosTramiteSealed()

    @Serializable
    data class RequisitoTramiteTempB(val requisitos: List<RequisitoTramiteB>) : RequisitosTramiteSealed()

    @Serializable
    data class RequisitoTramiteTempC(val array: List<RequisitoTramiteC>) : RequisitosTramiteSealed()

    @Serializable
    data class RequisitoTramiteTempD(val requisitos: List<RequisitoTramiteD>) : RequisitosTramiteSealed()

    @Serializable
    data object Vacio : RequisitosTramiteSealed()
}

object RequisitosSealedAsArraySerializer : KSerializer<RequisitosTramiteSealed> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("RequisitosSealedAsArray")

    override fun serialize(encoder: Encoder, value: RequisitosTramiteSealed) {
        val jsonEncoder = encoder as JsonEncoder
        val json = jsonEncoder.json

        val element = when (value) {
            is RequisitosTramiteSealed.RequisitoTramiteTempA ->
                JsonArray(value.requisitos.map {
                    json.encodeToJsonElement(RequisitoTramiteModoCheck.serializer(), it)
                })
            is RequisitosTramiteSealed.RequisitoTramiteTempB ->
                JsonArray(value.requisitos.map {
                    json.encodeToJsonElement(RequisitoTramiteB.serializer(), it)
                })
            is RequisitosTramiteSealed.RequisitoTramiteTempC ->
                JsonObject(
                    mapOf(
                        "array" to JsonArray(value.array.map {
                            json.encodeToJsonElement(RequisitoTramiteC.serializer(), it)
                        })
                    )
                )
            is RequisitosTramiteSealed.RequisitoTramiteTempD ->
                JsonArray(value.requisitos.map {
                    json.encodeToJsonElement(RequisitoTramiteD.serializer(), it)
                })
            is RequisitosTramiteSealed.Vacio ->
                JsonArray(emptyList())
        }
        jsonEncoder.encodeJsonElement(element)
    }

    override fun deserialize(decoder: Decoder): RequisitosTramiteSealed {
        throw UnsupportedOperationException("Solo serializacion")
    }
}
