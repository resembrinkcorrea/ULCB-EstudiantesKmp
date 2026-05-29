package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseComprobantePecano(
    @SerialName("EnlacePDF") val enlacePDF: String
)
