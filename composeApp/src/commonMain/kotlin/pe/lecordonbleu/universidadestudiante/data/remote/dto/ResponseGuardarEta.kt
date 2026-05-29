package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class guardar_documentos_eta(
    val icono: String,
    val tipo: Int,
    val contador: Int,
    val titulo: String,
    val mensaje: String
)

@Serializable
data class ResponseGuardarEta(
    val guardar_documentos_eta: List<guardar_documentos_eta> = emptyList(),
    val flag_val: Int
)
