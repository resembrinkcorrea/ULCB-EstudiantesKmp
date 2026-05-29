package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListPlanEstudio(
    val id_estud_pe: Int,
    val contador: Int,
    val id_pest_det: Int,
    val id_serv: Int,
    val pest_det_nombre: String
)

@Serializable
data class ResponsePlanEstudio(
    val ListPlanEstudio: List<ListPlanEstudio> = emptyList(),
    val flag_val: Int
)
