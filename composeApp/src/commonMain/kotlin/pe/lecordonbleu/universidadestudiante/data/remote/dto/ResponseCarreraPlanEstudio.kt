package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseCarreraPlanEstudio(
    val flag_val: Int,
    val ListPlanEstudioConv: List<ListPlanEstudioConv> = emptyList()
)

@Serializable
data class ListPlanEstudioConv(
    val id_estud_pe: Int,
    val contador: Int,
    val id_pest_det: Int,
    val id_serv: Int,
    val pest_det_nombre: String,
    val id_tiposerva: Int
)
