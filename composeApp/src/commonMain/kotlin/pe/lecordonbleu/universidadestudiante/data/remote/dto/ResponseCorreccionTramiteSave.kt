package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseCorreccionTramiteSave(
    val flag_val: Int,
    val CorreccionDocumentos: List<CorreccionDocumentos> = emptyList()
)

@Serializable
data class CorreccionDocumentos(
    val icono: String,
    val tipo: String,
    val titulo: String,
    val mensaje: String
)
