package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EliminarDocuEtaRes(
    val icono: String,
    val tipo: Int,
    val contador: Int,
    val titulo: String,
    val mensaje: String
)

@Serializable
data class ResponseEliminarDocEta(
    val EliminarDocuEtaRes: List<EliminarDocuEtaRes> = emptyList(),
    val flag_val: Int
)
