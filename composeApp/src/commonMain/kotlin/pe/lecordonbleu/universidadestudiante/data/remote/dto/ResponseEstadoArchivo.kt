package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseEstadoArchivo(
    val flag_val: Int,
    val ListadoArchivoEstado: List<ListadoArchivoEstado> = emptyList()
)

@Serializable
data class ListadoArchivoEstado(
    val tipo: Int,
    val icono: String,
    val titulo: String,
    val mensaje: String
)
