package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class registrar_marcacion(
    val icono: String,
    val tipo: Int,
    val titulo: String,
    val mensaje: String
)

@Serializable
data class ResponseMarcarAsistencia(
    val flag_val: Int,
    val registrar_marcacion: List<registrar_marcacion> = emptyList()
)
