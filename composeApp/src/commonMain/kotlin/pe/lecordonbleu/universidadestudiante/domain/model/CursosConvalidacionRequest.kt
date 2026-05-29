package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CursosConvalidacionRequest(
    val id_estud_pe: Int,
    val id_pest_det_destino: Int,
    val id_tipo_traslado: Int
)
