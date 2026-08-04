package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseRegistrarMatricula(
    val flag_val: Int = 0,
    val mensaje: String = "",
    val titulo: String? = null,
    val icono: String? = null,
    val tipo: Int? = null
)
