package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ValidarDocumentosRequest(
    val id_peracad: Int,
    val flag_ingresante: Int,
    val id_estud_pe: Int
)
