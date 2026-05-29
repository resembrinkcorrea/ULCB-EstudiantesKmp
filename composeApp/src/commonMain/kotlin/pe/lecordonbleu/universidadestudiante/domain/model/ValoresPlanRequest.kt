package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ValoresPlanRequest(
    val id_estud_pe: Int,
    val id_pest_det: Int,
    val id_serv: Int,
    val id_uneg: Int
)
