package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseQr(
    val codigo_qr: String,
    val flag: Int,
    val mensaje: String
)
