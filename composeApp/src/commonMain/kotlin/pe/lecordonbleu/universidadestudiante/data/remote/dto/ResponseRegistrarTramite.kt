package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseRegistrarTramite(
    val flag_val: Int,
    val RegistrarTramite: List<RegistrarTramite>? = emptyList()
)

@Serializable
data class RegistrarTramite(
    val icono: String,
    val tipo: String,
    val contador: Int,
    val titulo: String,
    val mensaje: String
)
