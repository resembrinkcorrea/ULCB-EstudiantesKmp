package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Periodo(
    val id_matric: String,
    val id_estud_pe: String,
    val id_pest_det: String,
    val id_oacad_arranque: String,
    val peracad_nombre: String,
    val id_peracad: String
)

@Serializable
data class ResponsePeriodo(
    val flag_val: Int,
    val periodo: List<Periodo> = emptyList()
)
