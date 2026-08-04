package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DataDocumentosIngresante(
    val estado: Int,
    val mensaje: String
)

@Serializable
data class ResponseValidarDocumentos(
    val flag_val: Int,
    val dataDocumentosIngresante: List<DataDocumentosIngresante> = emptyList()
)
