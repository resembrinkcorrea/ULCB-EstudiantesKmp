package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseComprobanteTramite(
    val statusCode: Int,
    val mensaje: String,
    val servicioUrl: String,
    val metodo: String,
    val excepcion: String?,
    val resultado: String
)
