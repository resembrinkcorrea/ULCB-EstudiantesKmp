package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseHora(
    val flag_val: Int,
    val listHoraServer: List<listHoraServer> = emptyList()
)

@Serializable
data class listHoraServer(
    val Hora: String,
    val fecha_dia: String,
    val datee: String
)
