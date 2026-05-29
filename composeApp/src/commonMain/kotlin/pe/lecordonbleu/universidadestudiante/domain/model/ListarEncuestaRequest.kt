package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ListarEncuestaRequest(
    val id_uneg: Int,
    val id_pest_det: Int,
    val id_peracad: Int
)
