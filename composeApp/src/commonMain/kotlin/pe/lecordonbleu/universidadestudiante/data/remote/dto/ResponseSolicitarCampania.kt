package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseSolicitarCampania(
    val flag_val: Int,
    val tipo: Int = 0,
    val icono: String = "",
    val titulo: String = "",
    val mensaje: String = ""
)
