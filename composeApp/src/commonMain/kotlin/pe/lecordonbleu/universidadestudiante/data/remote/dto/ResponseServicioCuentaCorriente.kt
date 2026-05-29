package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListServicioCorriente(
    val flag_extencion: Int,
    val nombre_servicio: String,
    val id_estud_pe: Int,
    val id_pest_det: Int,
    val plan_de_estudio: String,
    val id_estud_serv: Int,
    val id_tiposerva: Int,
    val id_servicio: Int
)

@Serializable
data class ResponseServicioCuentaCorriente(
    val flag_val: Int,
    val ListServicioCorriente: List<ListServicioCorriente> = emptyList()
)
